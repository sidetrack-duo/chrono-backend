package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {
    private boolean success;
    private String message;
    private String errorCode;

    public static ErrorResponseDto of(String message, String errorCode){
        return ErrorResponseDto.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}
