package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyCommitCountDto {
    private int dayOfWeek;
    private int count;
}
