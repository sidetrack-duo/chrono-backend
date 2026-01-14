package com.chrono.service;

import com.chrono.dto.CommitSummaryDto;
import com.chrono.dto.DailyCommitCountDto;
import com.chrono.dto.DashboardResponseDto;
import com.chrono.dto.WeeklyCommitCountDto;
import com.chrono.mapper.DashboardMapper;
import com.chrono.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<DailyCommitCountDto> getRecentDailyCommits() {
        Long userId = SecurityUtil.getCurrentUserId();

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);

        List<DailyCommitCountDto> dbResult =
                dashboardMapper.selectDailyCommitCounts(userId, start, end);

        Map<LocalDate, Integer> commitCountMap = dbResult.stream()
                .collect(Collectors.toMap(
                        DailyCommitCountDto::getDate,
                        DailyCommitCountDto::getCommitCount
                ));

        List<DailyCommitCountDto> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = start.plusDays(i);
            int count = commitCountMap.getOrDefault(date, 0);
            result.add(new DailyCommitCountDto(date, count));
        }

        return result;
    }

}
