package com.chrono.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "프로젝트 이름은 필수입니다.")
    private String title;
    private String description;

    private List<String> techStack;

    private LocalDate startDate;
    private LocalDate targetDate;
}
