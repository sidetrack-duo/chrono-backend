package com.chrono.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateProjectMetaDto {
    private String title;
    private String description;
    private List<String> techStack;
    private LocalDate startDate;
    private LocalDate targetDate;
}
