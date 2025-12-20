package com.chrono.dto;

import java.util.List;

public record  WeeklyAnalyzeRequestDto(Long projectId, List<String>commitDates) {
}
