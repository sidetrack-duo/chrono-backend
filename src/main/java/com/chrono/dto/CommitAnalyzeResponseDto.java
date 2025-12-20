package com.chrono.dto;

import java.time.LocalDateTime;

public record CommitAnalyzeResponseDto(
        int total,
        LocalDateTime latest,
        int weekly,
        String mostActiveDay
) {
}
