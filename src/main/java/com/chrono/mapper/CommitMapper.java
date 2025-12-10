package com.chrono.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Mapper
public interface CommitMapper {
    LocalDateTime findLatestCommitDate(Long projectId);
}
