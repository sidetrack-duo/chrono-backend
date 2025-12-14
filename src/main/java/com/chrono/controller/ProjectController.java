package com.chrono.controller;

import com.chrono.dto.CreateProjectRequestDto;
import com.chrono.dto.ProjectResponseDto;
import com.chrono.dto.UpdateProjectMetaDto;
import com.chrono.security.CustomUserDetailsService;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.ProjectService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public ResponseEntity<?> getProjects(@AuthenticationPrincipal CustomUserPrincipal principal){
        Long userId = principal.getUser().getUserId();
        return ResponseEntity.ok(projectService.getProjects(userId));
    }

    //프로젝트 직접 입력
    @PutMapping("/{projectId}/meta")
    public ResponseEntity<Void> updateProjectMeta(
            @PathVariable Long projectId,
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody UpdateProjectMetaDto req
    ) {
        projectService.updateProjectMeta(projectId, principal.getUser(), req);
        return ResponseEntity.ok().build();
    }

}
