package com.chrono.controller;

import com.chrono.dto.LoginRequestDto;
import com.chrono.dto.LoginResponseDto;
import com.chrono.dto.SignupRequestDto;
import com.chrono.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto requestDto){
        authService.signup(requestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto){
        LoginResponseDto response = authService.login(requestDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getRefreshTokenCookie())
                .body(Map.of(
                        "accessToken", response.getAccessToken(),
                        "nickname", response.getNickname()
                ));
    }
}
