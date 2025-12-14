package com.chrono.dto;

import com.chrono.enums.ProjectStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProjectStatusDto {
    private ProjectStatus status;
}
