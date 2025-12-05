package com.chrono.service;

import com.chrono.entity.RefreshTokenEntity;
import com.chrono.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    //로그인 시 저장, 업데이트
    public void saveOrUpdate(Long userId, String token){
        RefreshTokenEntity refreshToken = refreshTokenRepository.findById(userId)
                .orElse(RefreshTokenEntity.builder()
                        .userId(userId)
                        .token(token)
                        .build());
        refreshToken.updateToken(token);
        refreshTokenRepository.save(refreshToken);
    }

    //조회
    public RefreshTokenEntity getUserId(Long userId){
        return refreshTokenRepository.findById(userId)
                .orElse(null);
    }

    //삭제
    public void delete(Long userId){
        refreshTokenRepository.deleteById(userId);
    }
}
