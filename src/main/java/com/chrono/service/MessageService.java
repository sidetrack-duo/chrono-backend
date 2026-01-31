package com.chrono.service;

import com.chrono.entity.MessageEntity;
import com.chrono.entity.UserEntity;
import com.chrono.repository.MessageRepository;
import com.chrono.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
