package com.chrono.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePasswordRequestDto {
    @NotBlank(message = "비밀 번호 입력은 필수입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력하세요. ")
    @Size(min = 8, max = 20, message = "비밀번호는 숫자, 특수문자 포함 8~20로 설정합니다.")
    private String newPassword;

    @NotBlank(message = "비밀번호를 확인해주세요.")
    private String newPasswordConfirm;
}
