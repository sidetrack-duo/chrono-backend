package com.chrono.dto;

import com.chrono.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectDetailResponseDto {
    //기본 정보
    private Long projectId;
    private String owner;
    private String repoName;
    private String repoUrl;
    //메타 입력
    private String title;
    private String description;
    private List<String> techStack;
    private LocalDate startDate;
    private LocalDate targetDate;

    //상태
    private ProjectStatus status;
    private boolean active;
    private LocalDateTime createdAt;

    //커밋
    private int totalCommit;
    private LocalDateTime lastCommitAt;
}
