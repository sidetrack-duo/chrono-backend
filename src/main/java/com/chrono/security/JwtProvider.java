package com.chrono.security;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.time}")
    private long accessTokenExpireMs;

    @Value("${jwt.refresh.expiration.time}")
    private long refreshTokenExpireMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    //Access token
    public String createAccessToken(Long userId, String email){
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+accessTokenExpireMs))
                .signWith(getSigningKey())
                .compact();
    }

    //refresh token
    public String createRefreshToken(Long userId, String email){
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpireMs))
                .signWith(getSigningKey())
                .compact();
    }

    //refresh token > HTTPOnly Cookie
    public ResponseCookie createRefreshTokenCookie(String token){
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(refreshTokenExpireMs / 1000)
                .sameSite("Strict")
                .build();
    }
}
