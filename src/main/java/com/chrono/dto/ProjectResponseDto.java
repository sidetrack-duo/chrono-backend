package com.chrono.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {
    private Long projectId;
    private String owner;
    private String repoName;
    private String repoUrl;
    private boolean active;
    private LocalDateTime createdAt;
}
