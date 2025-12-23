package com.chrono.service;

import com.chrono.entity.UserEntity;
import com.chrono.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    //비번 재설정 요청
    public void requestReset(String email){
        UserEntity user = userRepository.findByEmailAndDeletedAtIsNull(email)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));

        String code = emailVerificationService.generateAndSaveCode(email);

        String html = EmailTemplate.passwordReset(code);

        emailSenderService.sendEmail(
                email,
                "[Chrono] 비밀번호 재설정 인증 코드",
                html
        );
    }

    //코드 검증 + 비번 변경
    @Transactional
    public void resetPassword(String email, String code, String newPassword){
        if(!emailVerificationService.verifyCode(email, code)){
            throw new IllegalArgumentException("인증 코드가 올바르지 않습니다.");
        }

        UserEntity user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));
        user.changePassword(passwordEncoder.encode(newPassword));
    }
}
