package com.chrono.dto;

import java.util.List;

public record ProjectIntroRequestDto(
        Long projectId,
        List<String> messages
) {
}
