package com.chrono.service;

import com.chrono.entity.EmailVerificationEntity;
import com.chrono.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationRepository verificationRepository;
    private final EmailSenderService emailSenderService;

    //인증코드
    private String generateCode() {
        int length = 8;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    //인증코드 생성, 저장, 발송
    @Transactional
    public void sendVerificationCode(String email){
        //새 코드 생성
        String code = generateCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);

        //디비 저장
        EmailVerificationEntity entity = EmailVerificationEntity.builder()
                .email(email)
                .validCode(code)
                .expiresAt(expiresAt)
                .verified(false)
                .build();
        verificationRepository.save(entity);

        String subject = "[Chrono] 이메일 인증코드 안내";
        String content = "인증코드: " + code + "\n5분 안에 입력해주세요.";

        emailSenderService.sendEmail(email, subject, content);
    }

    @Transactional
    public boolean verifyCode(String email, String code) {

        Optional<EmailVerificationEntity> optional =
                verificationRepository.findTopByEmailOrderByCreatedAtDesc(email);

        if (optional.isEmpty()) {
            return false;
        }

        EmailVerificationEntity entity = optional.get();

        //코드 일치 확인
        if (!entity.getValidCode().equals(code)) {
            return false;
        }

        //만료시간 확인
        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        //검증 성공
        entity.setVerified(true);
        verificationRepository.save(entity);

        return true;
    }

    //이메일 인증 완료한 계정인지 확인
    public boolean isEmailVerified(String email) {
        return verificationRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .map(EmailVerificationEntity::isVerified)
                .orElse(false);
    }
}
