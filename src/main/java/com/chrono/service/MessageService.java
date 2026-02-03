package com.chrono.service;

import com.chrono.dto.UserSearchResponseDto;
import com.chrono.entity.MessageEntity;
import com.chrono.entity.UserEntity;
import com.chrono.repository.MessageRepository;
import com.chrono.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    //쪽지 전송
    public void sendMessage(Long receiverId, String content, Long senderId){
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        UserEntity receiver = userRepository.findById(receiverId)
                .filter(user -> !user.isDeleted())
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자"));
        //셀프 쪽지 막기
        if(sender.getUserId().equals(receiverId)){
            throw new IllegalArgumentException("자기 자신에게는 쪽지를 보낼 수 없습니다.");
        }
        MessageEntity message = MessageEntity.send(sender, receiver, content);
        messageRepository.save(message);
    }

    //받은 쪽지 리스트 조회
    @Transactional(readOnly = true)
    public Page<MessageEntity> getReceiveMessageList(Long userId, Pageable pageable){

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        return messageRepository.findByReceiverAndDeletedByReceiverFalse(user, pageable);
    }

    //쪽지 상세 조회
    @Transactional
    public MessageEntity getMessageDetail(Long messageId, Long userId){
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(()-> new IllegalArgumentException("쪽지를 찾을 수 없습니다."));

        boolean isSender = message.getSender().getUserId().equals(userId);
        boolean isReceiver = message.getReceiver().getUserId().equals(userId);

        if (!isSender && !isReceiver) {
            throw new AccessDeniedException("쪽지 조회 권한 없음");
        }

        if (isSender && message.isDeletedBySender()) {
            throw new AccessDeniedException("삭제된 쪽지입니다.");
        }

        if (isReceiver && message.isDeletedByReceiver()) {
            throw new AccessDeniedException("삭제된 쪽지입니다.");
        }
        //수신자 읽음 처리
        if (isReceiver && !message.isRead()) {
            message.markAsRead();
        }

        return message;
    }

    //보낸 쪽지 리스트
    @Transactional(readOnly = true)
    public Page<MessageEntity> getSentMessageList(Long userId, Pageable pageable){

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        return messageRepository.findBySenderAndDeletedBySenderFalse(user, pageable);
    }

    //쪽지 개별 삭제
    @Transactional
    public void deleteMessage(Long messageId, Long userId){
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(()->new IllegalArgumentException("해당 쪽지가 없음"));
        message.deleteBy(userId);
    }

    //닉네임 검색
    @Transactional(readOnly = true)
    public Page<UserSearchResponseDto> searchUsers(String keyword, Long requesterId, Pageable pageable) {
        if(keyword == null || keyword.trim().isEmpty()){
            throw new IllegalArgumentException("검색어는 필수 입력해야 합니다.");
        }
        return messageRepository.searchUsers(keyword, requesterId, pageable);
    }

    // 안 읽은 쪽지 수
    @Transactional(readOnly = true)
    public long getUnreadMessageCount(Long userId){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자"));
        return messageRepository.countByReceiverAndIsReadFalseAndDeletedByReceiverFalse(user);
    }
}
