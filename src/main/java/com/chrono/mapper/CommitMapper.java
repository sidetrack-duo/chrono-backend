package com.chrono.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Mapper
public interface CommitMapper {
    //최근 커밋 날짜
    LocalDateTime findLatestCommitDate(Long projectId);

    int countTotalCommits(Long projectId);


    //이번주 커밋 수
    int countCommitsThisWeek(Long projectId);

    //가장 활발한 날
    String findMostActiveDay(Long projectId);
}
