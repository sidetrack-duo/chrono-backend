package com.chrono.controller;

import com.chrono.dto.EmailSendRequestDto;
import com.chrono.dto.EmailVerifyRequestDto;
import com.chrono.dto.SuccessResponseDto;
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
    public ResponseEntity<SuccessResponseDto<Void>> sendVerificationCode(@RequestBody EmailSendRequestDto request) {

        emailVerificationService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(SuccessResponseDto.ok());
    }

    // 인증코드 확인
    @PostMapping("/verify")
    public ResponseEntity<SuccessResponseDto<Boolean>> verifyCode(@RequestBody EmailVerifyRequestDto request) {

        boolean result = emailVerificationService.verifyCode(
                request.getEmail(),
                request.getCode()
        );

        return ResponseEntity.ok(
                SuccessResponseDto.ok(result)
        );
    }

}
