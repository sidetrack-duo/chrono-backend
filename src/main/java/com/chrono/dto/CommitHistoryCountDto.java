package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CommitHistoryCountDto {
    private LocalDate date;
    private int count;
}
