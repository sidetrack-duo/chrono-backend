package com.chrono.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectActiveDto {
    @NotNull(message = "활성 여부 값은 필수입니다.")
    private boolean active; //false가 삭제, true복구
}
