package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//github
@Data
@AllArgsConstructor
public class ValidationResponseDto {
    private boolean valid;
    private String username;
    private String avatarUrl;
    private String message;
}
