package com.chrono.controller;

import com.chrono.dto.GithubBasicConnectRequestDto;
import com.chrono.dto.GithubBasicConnectResponseDto;
import com.chrono.dto.ValidationResponseDto;
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
}
