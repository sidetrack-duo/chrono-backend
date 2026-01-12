package com.chrono.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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

    private final CustomUserDetailsService customUserDetailsService;

    private static final int MIN_SECRET_KEY_LENGTH = 32;

    @PostConstruct //서버 기동 시 즉시 검증
    public void validateSecretKey(){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8); //플랫폼 의존성 제거하기

        if(keyBytes.length<MIN_SECRET_KEY_LENGTH){
            throw new IllegalStateException(
                    "jwt secret key는 반드시 256비트여야 함"+
                            "현재 길이 : "+ keyBytes.length
            );
        }
    }

    public Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //Access token
    public String createAccessToken(Long userId, String email){
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+accessTokenExpireMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //refresh token
    public String createRefreshToken(Long userId, String email){
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpireMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
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

    //토큰 서명 만료 검증
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            log.debug("Jwt검증 실패 : {}", e.getMessage());
            return false;
        }
    }
    //securityContext에 authentication넣기
    public Authentication getAuthentication(String token){
        var claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.getSubject();
        String email = claims.get("email", String.class);

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
