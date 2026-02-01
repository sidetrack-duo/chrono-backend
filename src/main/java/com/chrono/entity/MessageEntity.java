package com.chrono.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="messages", indexes = {
        @Index(name = "idx_message_receiver", columnList = "receiver_id, created_at"),
        @Index(name = "idx_message_sender", columnList = "sender_id, created_at")
})
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    //발송인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    //수취인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiver_id", nullable = false)
    private UserEntity receiver;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private boolean isRead = false;

    @Column(nullable = false)
    private boolean deletedBySender = false;

    @Column(nullable = false)
    private boolean deletedByReceiver = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    //생성
    public static MessageEntity send(UserEntity sender, UserEntity receiver, String  content){
        MessageEntity message = new MessageEntity();
        message.sender = sender;
        message.receiver = receiver;
        message.content = content;
        return message;
    }

    //---------도메인 로직
    //읽음
    public void markAsRead() {
        this.isRead = true;
    }

    //각자 삭제 처리
    public void deleteBy(Long userId){
        if(sender.getUserId().equals(userId)){
            this.deletedBySender = true;
        }else if(receiver.getUserId().equals(userId)){
            this.deletedByReceiver = true;
        }else{
            throw new AccessDeniedException("삭제 권한 없음");
        }
    }

    public boolean isDeletedFor(UserEntity user){
        if(sender.getUserId().equals(user.getUserId())) return deletedBySender;
        if(receiver.getUserId().equals(user.getUserId())) return deletedByReceiver;
        return true;
    }
}
