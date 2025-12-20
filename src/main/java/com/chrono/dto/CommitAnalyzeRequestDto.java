package com.chrono.dto;

import java.util.List;

public record CommitAnalyzeRequestDto(
        Long projectId,
        List<CommitItemDto> commits
) {
    public record CommitItemDto(
            String sha,
            String date,
            String message
    ) {}
}
