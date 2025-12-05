package com.chrono.controller;

import com.chrono.dto.ValidationResponseDto;
import com.chrono.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
