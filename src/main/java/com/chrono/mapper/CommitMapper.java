package com.chrono.mapper;

import com.chrono.dto.CommitAnalyzeRequestDto;
import com.chrono.dto.CommitHistoryCountDto;
import com.chrono.dto.CommitResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommitMapper {
    //최근 커밋 날짜
    LocalDateTime findLatestCommitDate(Long projectId);

    //최근 14일 커밋
    List<CommitHistoryCountDto> selectDailyCommitHistory(Long projectId);

    //위클리 커밋
    List<String> findCommitDatesForAnalysis(Long projectId);

    List<CommitResponseDto> findAllCommitsByProject(Long projectId);

    List<CommitAnalyzeRequestDto.CommitItemDto> findCommitsForAnalysis(Long projectId);
}
