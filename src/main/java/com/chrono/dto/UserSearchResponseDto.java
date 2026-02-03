package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSearchResponseDto {
    private Long userId;
    private String nickname;
    private String email;
    private String githubUsername;
}
