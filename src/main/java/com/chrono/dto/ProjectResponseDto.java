package com.chrono.dto;


import com.chrono.entity.ProjectEntity;
import com.chrono.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDto {
    private Long projectId;
    private String owner;
    private String repoName;
    private String repoUrl;
    private boolean active;
    private LocalDateTime createdAt;

    private String title;
    private ProjectStatus status;
    private List<String> techStack;
    private Integer totalCommits;
    private LocalDateTime lastCommitAt;

    private LocalDate startDate;
    private LocalDate targetDate;

    //진행률 계산
    private  Integer progressRate;

    public static ProjectResponseDto fromEntity(ProjectEntity project){
        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .title(project.getTitle())
                .status(project.getStatus())
                .techStack(project.getTechStack())
                .owner(project.getOwner())
                .repoName(project.getRepoName())
                .repoUrl(project.getRepoUrl())
                .active(project.isActive())
                .totalCommits(project.getTotalCommits())
                .lastCommitAt(project.getLastCommitAt())
                .startDate(project.getStartDate())
                .targetDate(project.getTargetDate())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
