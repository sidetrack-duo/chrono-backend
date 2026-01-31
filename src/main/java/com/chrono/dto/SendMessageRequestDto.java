package com.chrono.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SendMessageRequestDto {
    @NotNull
    private Long receiverId;

    @NotBlank
    private String content;
}
