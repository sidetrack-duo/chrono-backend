package com.chrono.controller;

import com.chrono.dto.SendMessageRequestDto;
import com.chrono.dto.SuccessResponseDto;
import com.chrono.entity.UserEntity;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public SuccessResponseDto<Void> sendMessage(@Valid @RequestBody SendMessageRequestDto requestDto,
                                                @AuthenticationPrincipal CustomUserPrincipal principal){
        UserEntity sender = principal.getUser();

        messageService.sendMessage(
                requestDto.getReceiverId(),
                requestDto.getContent(),
                sender
        );
        return SuccessResponseDto.ok();
    }
}
