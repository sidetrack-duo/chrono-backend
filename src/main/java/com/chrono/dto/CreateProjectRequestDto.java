package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequestDto {
    private String owner;
    private String repoName;
    private String repoUrl;
}
