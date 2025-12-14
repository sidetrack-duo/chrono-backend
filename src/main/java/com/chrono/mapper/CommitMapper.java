package com.chrono.mapper;

import com.chrono.dto.CommitHistoryCountDto;
import com.chrono.dto.WeeklyCommitCountDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommitMapper {
    //최근 커밋 날짜
    LocalDateTime findLatestCommitDate(Long projectId);

    int countTotalCommits(Long projectId);

    //이번주 커밋 수
    int countCommitsThisWeek(Long projectId);

    //가장 활발한 날
    String findMostActiveDay(Long projectId);

    //위클리 커밋
    List<WeeklyCommitCountDto> findWeeklyCommitCount(@Param("projectId")Long projectId,
                                                     @Param("start")LocalDate start,
                                                     @Param("end") LocalDate end);

    List<CommitHistoryCountDto> findCommitHistory(@Param("projectId")Long projectId,
                                                  @P("start")LocalDate start);
}
