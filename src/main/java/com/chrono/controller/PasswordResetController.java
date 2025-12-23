package com.chrono.controller;

import com.chrono.dto.PasswordResetConfirmDto;
import com.chrono.dto.PasswordResetRequestDto;
import com.chrono.dto.SuccessResponseDto;
import com.chrono.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/password")
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    //비번 재설정 요청
    @PostMapping("/reset-request")
    public SuccessResponseDto<Void> requestReset(@RequestBody @Valid PasswordResetRequestDto request){

        passwordResetService.requestReset(request.getEmail());

        return SuccessResponseDto.ok();
    }

    //코드 확인, 비번 변경
    @PostMapping("/reset")
    public SuccessResponseDto<Void> resetPassword(@RequestBody @Valid PasswordResetConfirmDto dto){

        passwordResetService.resetPassword(
                dto.getEmail(),
                dto.getCode(),
                dto.getNewPassword()
        );
        return SuccessResponseDto.ok();
    }
}
