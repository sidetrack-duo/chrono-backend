package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubRepoDto {
    private Long repoId;
    private String repoName;
    private String fullName; //유저명+레포명
    private String description;
    private boolean isPrivate;
    private String htmlUrl; //레포링크
    private String language;
    private int stargazersCount;
    private int forksCount;
    private String updatedAt;
}
