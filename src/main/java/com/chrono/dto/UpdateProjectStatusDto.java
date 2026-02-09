package com.chrono.dto;

import com.chrono.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProjectStatusDto {
    @NotNull(message = "상태 값은 필수입니다.")
    private ProjectStatus status;
}
