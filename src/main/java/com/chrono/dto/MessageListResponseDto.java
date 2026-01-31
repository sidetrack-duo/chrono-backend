package com.chrono.dto;

import com.chrono.entity.MessageEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageListResponseDto {
    private Long messageId;
    private Long senderId;
    private String senderNickname;
    private String content;
    private boolean read;
    private LocalDateTime createdAt;

    public static MessageListResponseDto from(MessageEntity message){
        return MessageListResponseDto.builder()
                .messageId(message.getMessageId())
                .senderId(message.getSender().getUserId())
                .senderNickname(message.getSender().getNickname())
                .content(message.getContent())
                .read(message.isRead())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
