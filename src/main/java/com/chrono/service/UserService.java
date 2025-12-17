package com.chrono.service;

import com.chrono.dto.UpdatePasswordRequestDto;
import com.chrono.entity.UserEntity;
import com.chrono.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updatePassword(UserEntity user, UpdatePasswordRequestDto req){
        // 비번 검증
        if(!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())){
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }
        // 새 비번, 비번 확인
        if (!req.getNewPassword().equals(req.getNewPasswordConfirm())) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }
        // 기존 비번 동일 체크
        if (passwordEncoder.matches(req.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일한 비밀번호입니다.");
        }
        // 새 비밀번호 암호화 + 저장
        String encodedPassword = passwordEncoder.encode(req.getNewPassword());
        user.updatePassword(encodedPassword);
    }
}
