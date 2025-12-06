package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubBasicConnectResponseDto {
    private boolean connected;
    private String type;
    private String username;
    private String avatarUrl;
    private String message;
}
