package com.chrono.controller;

import com.chrono.dto.CreateProjectRequestDto;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<?> createProject(
            @RequestBody CreateProjectRequestDto req,
            @AuthenticationPrincipal CustomUserPrincipal principal){

        Long userId = principal.getUser().getUserId();
        Long projectId = projectService.createProject(userId,req);
        return ResponseEntity.ok(Map.of("projectId", projectId));
    }
}
