package com.chrono.repository;

import com.chrono.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {

    //이메일로 최근 인증 요청 찾기
    Optional<EmailVerificationEntity> findTopByEmailOrderByCreatedAtDesc(String email);

    //이메일 + 코드로 조회
    Optional<EmailVerificationEntity> findByEmailAndValidCode(String email, String validCode);
}
