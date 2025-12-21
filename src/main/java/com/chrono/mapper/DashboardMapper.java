package com.chrono.mapper;

import com.chrono.dto.CommitSummaryDto;
import com.chrono.dto.WeeklyCommitCountDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardMapper {
    //진행 중, 완료 프로젝트
    int countInProgressProjects(@Param("userId") Long userId);
    int countCompletedProjects(@Param("userId")Long userId);

    //월 커밋 수
    int countCommitsThisMonth(@Param("userId") Long userId);

    //요일별 커밋 수
    List<WeeklyCommitCountDto> selectWeeklyCommitCounts(
            @Param("userId") Long userId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
            );

    //최근 플젝 + 커밋 요약
    List<CommitSummaryDto> selectRecentProjectSummaries(@Param("userId")Long userId);
}
