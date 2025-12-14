package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyCommitDto {
    private String day;
    private int count;
}
