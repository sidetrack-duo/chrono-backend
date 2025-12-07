package com.chrono.controller;

import com.chrono.dto.*;
import com.chrono.entity.UserEntity;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.GithubRepositoryService;
import com.chrono.service.GithubService;
import com.sun.security.auth.UserPrincipal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;
    private final GithubRepositoryService getRepositories;

    @GetMapping("/validate")
    public ResponseEntity<ValidationResponseDto> validate(@RequestParam String username){
        ValidationResponseDto result = githubService.validateUsername(username);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/connect-basic")
    public ResponseEntity<GithubBasicConnectResponseDto> connectBasic(
            @RequestBody GithubBasicConnectRequestDto request){
        return ResponseEntity.ok(githubService.connectBasic(request.getUsername()));
    }

    @PostMapping("/connect-pat")
    public ResponseEntity<GithubPatConnectResponseDto> connectPat(
            @RequestBody GithubPatConnectRequestDto requestDto) {
        return ResponseEntity.ok(githubService.connectPat(requestDto.getUsername(), requestDto.getPat()));
    }

    @GetMapping("/repos")
    public ResponseEntity<List<GithubRepoDto>> getRepositories(
            @AuthenticationPrincipal CustomUserPrincipal principal){
        UserEntity user = principal.getUser();
        return ResponseEntity.ok(getRepositories.getRepositories(user));

    }
}
