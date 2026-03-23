package com.chrono.dto;

import java.util.List;

public record AiSummaryRequestDto(
        Long projectId,
        List<String> messages
) {}
