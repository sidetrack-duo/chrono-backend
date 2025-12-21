package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardResponseDto {
    private Summary summary;
    private List<WeeklyCommitCountDto> weeklyCommits;
    private WeekInfo weekInfo;
    private List<CommitSummaryDto> recentProjects;

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private int inProgressCount;
        private int completedCount;
        private int totalCommitsThisMonth;
    }
    @Getter
    @AllArgsConstructor
    public static class WeekInfo {
        private LocalDate startDate;
        private LocalDate endDate;
    }

}
