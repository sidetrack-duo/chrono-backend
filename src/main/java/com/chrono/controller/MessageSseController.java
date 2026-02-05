package com.chrono.controller;

import com.chrono.security.CustomUserPrincipal;
import com.chrono.sse.MessageSseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageSseController {
    private final MessageSseManager messageSseManager;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserPrincipal principal){
        Long userId = principal.getUser().getUserId();
        return messageSseManager.connect(userId);
    }
}
