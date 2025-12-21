package com.chrono.service;

import com.chrono.dto.CommitSummaryDto;
import com.chrono.dto.DashboardResponseDto;
import com.chrono.dto.WeeklyCommitCountDto;
import com.chrono.mapper.DashboardMapper;
import com.chrono.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardMapper dashboardMapper;

    public DashboardResponseDto getDashboard(){
        Long userId = SecurityUtil.getCurrentUserId();

        int inProgress = dashboardMapper.countInProgressProjects(userId);
        int completed = dashboardMapper.countCompletedProjects(userId);
        int commitsThisMonth = dashboardMapper.countCommitsThisMonth(userId);

        LocalDate start = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate end = LocalDate.now().with(DayOfWeek.SUNDAY);

        List<WeeklyCommitCountDto> weekly = dashboardMapper.selectWeeklyCommitCounts(
                userId, start.toString(), end.toString());

        List<CommitSummaryDto> recent = dashboardMapper.selectRecentProjectSummaries(userId);

        DashboardResponseDto.Summary summary = new DashboardResponseDto.Summary(
                inProgress, completed, commitsThisMonth
        );

        DashboardResponseDto.WeekInfo weekInfo = new DashboardResponseDto.WeekInfo(start, end);

        return new DashboardResponseDto(summary, weekly, weekInfo, recent);
    }
}
