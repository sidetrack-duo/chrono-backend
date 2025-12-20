package com.chrono.dto;

import java.util.List;

public record  HistoryAnalyzeRequestDto(Long projectId,
                                        List<String> commitDates) {
}
