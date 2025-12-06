package com.chrono.controller;

import com.chrono.dto.*;
import com.chrono.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;

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
}
