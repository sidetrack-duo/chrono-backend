package com.chrono.service;

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
    public void sendMessage(Long receiverId, String content, UserEntity sender){
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
    public Page<MessageEntity> getReceiveMessageList(UserEntity user, Pageable pageable){
        return messageRepository.findByReceiverAndDeletedByReceiverFalse(user, pageable);
    }

    //쪽지 상세 조회
    @Transactional
    public MessageEntity getMessageDetail(Long messageId, Long userId){
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(()-> new IllegalArgumentException("쪽지를 찾을 수 없습니다."));

        Long senderId = message.getSender().getUserId();
        Long receiverId = message.getReceiver().getUserId();

        if (!senderId.equals(userId) && !receiverId.equals(userId)) {
            throw new AccessDeniedException("쪽지 조회 권한 없음");
        }
        //수신자 읽음 처리
        if (receiverId.equals(userId) && !message.isRead()) {
            message.markAsRead();
        }

        return message;
    }

    //보낸 쪽지 리스트
    @Transactional(readOnly = true)
    public Page<MessageEntity> getSentMessageList(UserEntity user, Pageable pageable){


        return messageRepository.findBySenderAndDeletedBySenderFalse(user, pageable);
    }
}
