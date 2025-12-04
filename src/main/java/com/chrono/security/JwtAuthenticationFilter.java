package com.chrono.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        String requestURI = request.getRequestURI();
        log.debug("***JwtAuthenticationFilter 실행 - URI: {}", requestURI);

        //토큰 추출
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            String token = bearerToken.substring(7);

            try{
                //토큰 유효성 검증
                if(jwtProvider.validateToken(token)){
                    //authentication생성
                    Authentication authentication = jwtProvider.getAuthentication(token);
                    //securityContext저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("securityContext에 인증 정보 저장, 사용자: {}", authentication.getName());
                }
            }catch (Exception e){
                log.warn("jwt인증 실패:{}" ,e.getMessage());
            }
        }else {
            log.debug("Authentication 헤더에 bearer token없음");
        }

        filterChain.doFilter(request, response);
    }
}
