package com.chrono.service;

import com.chrono.dto.SignupRequestDto;
import com.chrono.entity.UserEntity;
import com.chrono.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    //회원가입
    public void signup(SignupRequestDto request){
        String email = request.getEmail();
        String nickname = request.getNickname();
        String password = request.getPassword();

        //이메일 인증 체크
        if(!emailVerificationService.isEmailVerified(email)){
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }
        //활성 사용자 이메일 중복검사
        if(userRepository.existsByEmailAndDeletedAtIsNull(email)){
            throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
        }
        //닉네임 중복
        if(userRepository.existsByNicknameAndDeletedAtIsNull(nickname)){
            throw new IllegalArgumentException("이미 사용 중인 닉네임 입니다.");
        }
        //비밀번호 조건 검증
        validatePassword(password);

        //비번 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = UserEntity.builder()
                .email(email)
                .nickname(nickname)
                .password(encodedPassword)
                .deletedAt(null)//활성 사용자 구분
                .build();
        userRepository.save(user);

        log.info("회원가입 완료: email={}, nickname={}", email, nickname);
    }

    //비밀번호 조건
    private void validatePassword(String password){
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|;:'\",.<>/?`~]).{8,}$")) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이며 영문, 숫자, 특수문자를 포함해야 합니다.");
        }
    }
}
