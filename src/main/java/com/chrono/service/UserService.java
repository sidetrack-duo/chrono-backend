package com.chrono.service;

import com.chrono.dto.UpdateNicknameRequestDto;
import com.chrono.dto.UpdatePasswordRequestDto;
import com.chrono.dto.UserInfoResponseDto;
import com.chrono.entity.UserEntity;
import com.chrono.mapper.UserMapper;
import com.chrono.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

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

    //정보조회
    public UserInfoResponseDto getMyInfo(UserEntity user){
        if (user == null) {
            throw new EntityNotFoundException("유저가 없습니다.");
        }

        return new UserInfoResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getGithubUsername()
        );
    }

    //프로필 닉네임 수정
    @Transactional
    public  UserInfoResponseDto updatedNickname (UserEntity user, UpdateNicknameRequestDto req){
        if(user == null){
            throw new EntityNotFoundException("유저가 없습니다.");
        }
        int updated = userMapper.updateNickname(
                user.getUserId(),
                req.getNickname()
        );

        if(updated == 0){
            throw new IllegalArgumentException("닉네임 수정에 실패했습니다.");
        }
        UserEntity updatedUser = userMapper.selectById(user.getUserId());

        return new UserInfoResponseDto(
                updatedUser.getUserId(),
                updatedUser.getEmail(),
                updatedUser.getNickname(),
                updatedUser.getGithubUsername()
        );
    }
}
