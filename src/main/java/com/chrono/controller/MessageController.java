package com.chrono.controller;

import com.chrono.dto.MessageDetailResponseDto;
import com.chrono.dto.MessageListResponseDto;
import com.chrono.dto.SendMessageRequestDto;
import com.chrono.dto.SuccessResponseDto;
import com.chrono.entity.MessageEntity;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public SuccessResponseDto<Void> sendMessage(@Valid @RequestBody SendMessageRequestDto requestDto,
                                                @AuthenticationPrincipal CustomUserPrincipal principal){

        messageService.sendMessage(
                requestDto.getReceiverId(),
                requestDto.getContent(),
                principal.getUser().getUserId()
        );
        return SuccessResponseDto.ok();
    }

    @GetMapping("/inbox")
    public SuccessResponseDto<Page<MessageListResponseDto>> getReceiveMessageList(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            Pageable pageable){

        Page<MessageListResponseDto> result = messageService.getReceiveMessageList(principal.getUser().getUserId(), pageable)
                .map(MessageListResponseDto::fromInbox);
        return SuccessResponseDto.ok(result);
    }

    @GetMapping("/{messageId}")
    public SuccessResponseDto<MessageDetailResponseDto> getMessageDetail(
            @PathVariable Long messageId,
            @AuthenticationPrincipal CustomUserPrincipal principal){

        MessageEntity message =
                messageService.getMessageDetail(messageId, principal.getUser().getUserId());

        return SuccessResponseDto.ok(MessageDetailResponseDto.from(message));
    }

    @GetMapping("/sent")
    public SuccessResponseDto<Page<MessageListResponseDto>> getSentMessageList(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            Pageable pageable){

        Page<MessageListResponseDto> result =
                messageService.getSentMessageList(principal.getUser().getUserId(), pageable)
                .map(MessageListResponseDto::fromSent);
        return SuccessResponseDto.ok(result);
    }

    @DeleteMapping("/{messageId}")
    public SuccessResponseDto<Void> deleteMessage(@PathVariable Long messageId,
                                                  @AuthenticationPrincipal CustomUserPrincipal principal){
        messageService.deleteMessage(messageId, principal.getUser().getUserId());
        return SuccessResponseDto.ok();
    }
}
