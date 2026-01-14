package com.chrono.controller;

import com.chrono.dto.DailyCommitCountDto;
import com.chrono.dto.DashboardResponseDto;
import com.chrono.dto.SuccessResponseDto;
import com.chrono.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public SuccessResponseDto<DashboardResponseDto> getDashboard(){
        return SuccessResponseDto.ok(dashboardService.getDashboard());
    }

    @GetMapping("/recent7days")
    public SuccessResponseDto<List<DailyCommitCountDto>> getRecentDailyCommits(){
        return SuccessResponseDto.ok(dashboardService.getRecentDailyCommits());
    }
}
