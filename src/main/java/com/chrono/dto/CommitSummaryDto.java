package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommitSummaryDto {
    private Long projectId;
    private int totalCommits;
    private LocalDateTime latestCommitDate;
    private int commitsThisWeek;
    private String mostActiveDay;
}
