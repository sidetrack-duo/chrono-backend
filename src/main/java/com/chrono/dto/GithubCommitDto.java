package com.chrono.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GithubCommitDto {
    private String sha;
    private CommitInfo commit;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommitInfo {
        private Author author;
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Author {
        private String name;
        private String email;
        private LocalDateTime date;
    }


}
