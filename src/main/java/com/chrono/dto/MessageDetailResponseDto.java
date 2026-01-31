package com.chrono.dto;

import com.chrono.entity.MessageEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageDetailResponseDto {
    private Long messageId;
    private Long senderId;
    private String senderNickname;
    private Long receiverId;
    private String receiverNickname;
    private String content;
    private boolean read;
    private LocalDateTime createdAt;

    public static MessageDetailResponseDto  from(MessageEntity message){
        return MessageDetailResponseDto .builder()
                .messageId(message.getMessageId())
                .senderId(message.getSender().getUserId())
                .senderNickname(message.getSender().getNickname())
                .receiverId(message.getReceiver().getUserId())
                .receiverNickname(message.getReceiver().getNickname())
                .content(message.getContent())
                .read(message.isRead())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
