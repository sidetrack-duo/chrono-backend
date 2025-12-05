package com.chrono.controller;

import com.chrono.dto.LoginRequestDto;
import com.chrono.dto.LoginResponseDto;
import com.chrono.dto.SignupRequestDto;
import com.chrono.entity.RefreshTokenEntity;
import com.chrono.security.JwtProvider;
import com.chrono.service.AuthService;
import com.chrono.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

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

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value="refreshToken", required = false)
                                     String refreshToken){
        //쿠키존재여부 확인
        if(refreshToken == null){
            return ResponseEntity.status(401).body("리프레시 토큰이 없음");
        }
        //토큰 유효성 검증
        if(!jwtProvider.validateToken(refreshToken)){
            return ResponseEntity.status(401).body("토큰이 유효하지 않음");
        }
        //사용자 정보 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtProvider.getSigningKey())
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        Long userId = Long.valueOf(claims.getSubject());
        String email = claims.get("email", String.class);

        //디비 일치 여부
        RefreshTokenEntity saved = refreshTokenService.getUserId(userId);

        if(saved == null || !saved.getToken().equals(refreshToken)){
            return ResponseEntity.status(401).body("토큰이 일치하지 않음");
        }

        //access새로 발급
        String newAccessToken = jwtProvider.createAccessToken(userId, email);
        return ResponseEntity.ok(newAccessToken);
    }
}
