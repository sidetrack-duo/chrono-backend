package com.chrono.controller;

import com.chrono.dto.SuccessResponseDto;
import com.chrono.dto.UpdatePasswordRequestDto;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/me/password")
    public ResponseEntity<SuccessResponseDto<Void>> updatePassword(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody UpdatePasswordRequestDto req
    ) {
        userService.updatePassword(principal.getUser(), req);
        return ResponseEntity.ok(SuccessResponseDto.ok());
    }
}
