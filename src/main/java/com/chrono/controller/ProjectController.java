package com.chrono.controller;

import com.chrono.dto.*;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<SuccessResponseDto<Long>> createProject(
            @RequestBody CreateProjectRequestDto req,
            @AuthenticationPrincipal CustomUserPrincipal principal){

        Long projectId = projectService.createProject(principal.getUser(),req);
        return ResponseEntity.ok(SuccessResponseDto.ok(projectId));
    }

    @GetMapping
    public ResponseEntity<SuccessResponseDto<List<ProjectResponseDto>>> getProjects(
            @AuthenticationPrincipal CustomUserPrincipal principal){
        return ResponseEntity.ok(
                SuccessResponseDto.ok(projectService.getProjects(principal.getUser()))
        );
    }

    //프로젝트 직접 입력
    @PutMapping("/{projectId}/meta")
    public ResponseEntity<SuccessResponseDto<Void>> updateProjectMeta(
            @PathVariable Long projectId,
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody UpdateProjectMetaDto req
    ) {
        projectService.updateProjectMeta(projectId, principal.getUser(), req);
        return ResponseEntity.ok(SuccessResponseDto.ok());
    }

    //상태변경
    @PatchMapping("/{projectId}/status")
    public ResponseEntity<SuccessResponseDto<Void>> updateProjectStatus(@PathVariable Long projectId,
                                                 @AuthenticationPrincipal CustomUserPrincipal principal,
                                                 @RequestBody UpdateProjectStatusDto req){
        projectService.updateProjectStatus(projectId, principal.getUser(), req);
        return ResponseEntity.ok(SuccessResponseDto.ok());
    }

    //상세조회
    @GetMapping("/{projectId}")
    public ResponseEntity<SuccessResponseDto<ProjectDetailResponseDto>> getProjectDetail(
            @PathVariable Long projectId, @AuthenticationPrincipal CustomUserPrincipal principal){
        return ResponseEntity.ok(
                SuccessResponseDto.ok(projectService.getProjectDetail(projectId, principal.getUser())));
    }

    //상태변경(활, 비활)
    @PatchMapping("/{projectId}/active")
    public ResponseEntity<SuccessResponseDto<Void>> updateProjectActive(@PathVariable Long projectId,
                                                    @AuthenticationPrincipal CustomUserPrincipal principal,
                                                    @RequestBody UpdateProjectActiveDto req){
        projectService.updateProjectActive(projectId, principal.getUser(), req);
        return ResponseEntity.ok(SuccessResponseDto.ok());
    }
}
