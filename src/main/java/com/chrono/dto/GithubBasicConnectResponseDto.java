package com.chrono.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubBasicConnectResponseDto {
    private boolean connected;
    private String type;

    @NotBlank(message = "Github 사용자명은 필수입니다.")
    private String username;

    private String avatarUrl;
    private String message;
}
