package com.chrono.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateNicknameRequestDto {
    @NotBlank(message = "닉네임 입력은 필수")
    @Size(min = 2, message = "닉네임은 최소 2자 이상")
    private String nickname;
}
