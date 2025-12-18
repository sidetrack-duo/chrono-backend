package com.chrono.controller;

import com.chrono.dto.*;
import com.chrono.entity.UserEntity;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.GithubRepositoryService;
import com.chrono.service.GithubService;
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
    private final GithubRepositoryService githubRepositoryService;

    @GetMapping("/validate")
    public ResponseEntity<SuccessResponseDto<ValidationResponseDto>> validate(@RequestParam String username){
        ValidationResponseDto result = githubService.validateUsername(username);

        return ResponseEntity.ok(SuccessResponseDto.ok(result));
    }

    @PostMapping("/connect-basic")
    public ResponseEntity<SuccessResponseDto<GithubBasicConnectResponseDto>> connectBasic(
            @RequestBody GithubBasicConnectRequestDto request){

        return ResponseEntity.ok(
                SuccessResponseDto.ok(githubService.connectBasic(request.getUsername())));
    }

    @PostMapping("/connect-pat")
    public ResponseEntity<SuccessResponseDto<GithubPatConnectResponseDto>> connectPat(
            @RequestBody GithubPatConnectRequestDto requestDto) {

        return ResponseEntity.ok(SuccessResponseDto.ok(
                        githubService.connectPat(requestDto.getUsername(), requestDto.getPat())));
    }

    @DeleteMapping("/pat")
    public ResponseEntity<SuccessResponseDto<GithubPatConnectResponseDto>> deletePat(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        return ResponseEntity.ok(
                SuccessResponseDto.ok(githubService.deletePat(principal.getUser())));
    }

    @GetMapping("/repos")
    public ResponseEntity<SuccessResponseDto<List<GithubRepoDto>>> getRepositories(
            @AuthenticationPrincipal CustomUserPrincipal principal){
        UserEntity user = principal.getUser();

        return ResponseEntity.ok(
                SuccessResponseDto.ok(githubRepositoryService.getRepositories(user)));
    }
}
