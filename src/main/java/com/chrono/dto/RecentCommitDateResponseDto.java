package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecentCommitDateResponseDto {
    private Long projectId;
    private String sha;
    private String message;
    private String authorName;
    private LocalDateTime commitDate;
}
