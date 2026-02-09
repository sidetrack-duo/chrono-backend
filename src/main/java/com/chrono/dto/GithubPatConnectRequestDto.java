package com.chrono.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GithubPatConnectRequestDto {
    @NotBlank(message = "github 사용자명은 필수입니다.")
    private String username;

    @NotBlank(message = "PAT는 필수입니다.")
    private String pat;
}
