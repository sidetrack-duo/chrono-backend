package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuccessResponseDto<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> SuccessResponseDto<T> ok (T data){
        return SuccessResponseDto.<T>builder()
                .success(true)
                .message("SUCCESS")
                .data(data)
                .build();
    }

    public static SuccessResponseDto<Void> ok() {
        return SuccessResponseDto.<Void>builder()
                .success(true)
                .message("SUCCESS")
                .build();
    }
}
