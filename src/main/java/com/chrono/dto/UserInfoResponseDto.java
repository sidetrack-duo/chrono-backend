package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private String githubUsername;
}
