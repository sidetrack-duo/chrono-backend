package com.chrono.controller;

import com.chrono.dto.LoginRequestDto;
import com.chrono.dto.LoginResponseDto;
import com.chrono.dto.SignupRequestDto;
import com.chrono.dto.SuccessResponseDto;
import com.chrono.entity.RefreshTokenEntity;
import com.chrono.security.JwtProvider;
import com.chrono.service.AuthService;
import com.chrono.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponseDto<Void>> signup(@RequestBody SignupRequestDto requestDto){
        authService.signup(requestDto);

        return ResponseEntity.ok(SuccessResponseDto.ok());
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto<LoginResponseDto>> login(@RequestBody LoginRequestDto requestDto){
        LoginResponseDto response = authService.login(requestDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getRefreshTokenCookie())
                .body(SuccessResponseDto.ok(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponseDto<String>> refresh(@CookieValue(value="refreshToken", required = false)
                                     String refreshToken){
        //쿠키존재여부 확인
        if(refreshToken == null){
            return ResponseEntity.status(401).body(SuccessResponseDto.ok(null));
        }
        //토큰 유효성 검증
        if(!jwtProvider.validateToken(refreshToken)){
            return ResponseEntity.status(401).body(SuccessResponseDto.ok(null));
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
            return ResponseEntity.status(401)
                    .body(SuccessResponseDto.ok(null));
        }

        //access새로 발급
        String newAccessToken = jwtProvider.createAccessToken(userId, email);

        return ResponseEntity.ok(SuccessResponseDto.ok(newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponseDto<Void>> logout(@AuthenticationPrincipal UserDetails user){
        Long userId = Long.valueOf(user.getUsername());

        //db에서 refresh삭제
        refreshTokenService.delete(userId);

        //쿠키 삭제
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(SuccessResponseDto.ok());
    }
}
