package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubPatConnectResponseDto {
    private boolean  connected;
    private String type; //FULL
    private String message;
}
