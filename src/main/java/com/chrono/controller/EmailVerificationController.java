package com.chrono.controller;

import com.chrono.dto.EmailSendRequest;
import com.chrono.dto.EmailVerifyRequest;
import com.chrono.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;

    // 인증코드 발송
    @PostMapping("/send")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailSendRequest request) {

        emailVerificationService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("인증코드를 전송했습니다.");
    }

    // 인증코드 확인
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody EmailVerifyRequest request) {

        boolean result = emailVerificationService.verifyCode(
                request.getEmail(),
                request.getCode()
        );

        if (result) {
            return ResponseEntity.ok("이메일 인증 성공");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증 실패");
        }
    }

}
