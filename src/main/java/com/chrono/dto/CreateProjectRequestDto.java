package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequestDto {
    private String owner;
    private String repoName;
    private String repoUrl;

    private String title;
    private String description;

    private List<String> techStack;

    private LocalDate startDate;
    private LocalDate targetDate;
}
