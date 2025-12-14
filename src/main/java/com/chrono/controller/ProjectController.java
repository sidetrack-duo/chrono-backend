package com.chrono.controller;

import com.chrono.dto.CreateProjectRequestDto;
import com.chrono.dto.ProjectDetailResponseDto;
import com.chrono.dto.UpdateProjectMetaDto;
import com.chrono.dto.UpdateProjectStatusDto;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        Long projectId = projectService.createProject(principal.getUser(),req);
        return ResponseEntity.ok(Map.of("projectId", projectId));
    }

    @GetMapping
    public ResponseEntity<?> getProjects(@AuthenticationPrincipal CustomUserPrincipal principal){
        return ResponseEntity.ok(projectService.getProjects(principal.getUser()));
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

    //상태변경
    @PatchMapping("/{projectId}/status")
    public ResponseEntity<?> updateProjectStatus(@PathVariable Long projectId,
                                                 @AuthenticationPrincipal CustomUserPrincipal principal,
                                                 @RequestBody UpdateProjectStatusDto req){
        projectService.updateProjectStatus(projectId, principal.getUser(), req);
        return ResponseEntity.ok().build();
    }

    //상세조회
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponseDto> getProjectDetail(
            @PathVariable Long projectId, @AuthenticationPrincipal CustomUserPrincipal principal){
        return ResponseEntity.ok(projectService.getProjectDetail(projectId, principal.getUser()));
    }

}
