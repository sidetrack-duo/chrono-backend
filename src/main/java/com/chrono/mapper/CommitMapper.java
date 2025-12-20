package com.chrono.mapper;

import com.chrono.dto.CommitAnalyzeRequestDto;
import com.chrono.dto.CommitHistoryCountDto;
import com.chrono.dto.CommitResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    List<String> findCommitDatesForAnalysis(Long projectId);

    List<CommitHistoryCountDto> findCommitHistory(@Param("projectId")Long projectId,
                                                  @Param("start")LocalDate start);

    List<CommitResponseDto> findAllCommitsByProject(Long projectId);

    List<CommitAnalyzeRequestDto.CommitItemDto> findCommitsForAnalysis(Long projectId);
}
