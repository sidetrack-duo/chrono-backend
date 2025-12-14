package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommitResponseDto {
    private String sha;
    private String message;
    private String authorName;
    private String authorEmail;
    private LocalDateTime commitDate;
}
