package com.chrono.controller;

import com.chrono.dto.CommitResponseDto;
import com.chrono.dto.CommitSummaryDto;
import com.chrono.dto.WeeklyCommitCountDto;
import com.chrono.security.CustomUserPrincipal;
import com.chrono.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class CommitController {
    private final CommitService commitService;

    //커밋 동기화
    @PostMapping("/{projectId}/commits/sync")
    public ResponseEntity<?> syncCommits(@PathVariable Long projectId){
        int savedCount = commitService.syncCommits(projectId);

        return ResponseEntity.ok(Map.of(
                "message", "커밋 동기화 완료",
                "savedCount", savedCount
        ));
    }

    //커밋 수 조회
    @GetMapping("/{projectId}/commits/count")
    public ResponseEntity<?> getCommitCount(@PathVariable Long projectId){
        int count = commitService.getCommitCount(projectId);

        return ResponseEntity.ok(Map.of(
                "projectId", projectId,
                "totalCommits", count)
        );
    }

    //최근 커밋 날짜 조회
    @GetMapping("/{projectId}/commits/latest")
    public  ResponseEntity<?> getLatestCommit(@PathVariable Long projectId){
        LocalDateTime latest = commitService.getLatestCommitDate(projectId);

        return ResponseEntity.ok(Map.of(
                "projectId", projectId,
                "latestCommitDate", latest
        ));
    }

    //커밋 통계
    @GetMapping("/{projectId}/commits/summary")
    public ResponseEntity<?> getCommitSummary(@PathVariable Long projectId){

        CommitSummaryDto summary = commitService.getCommitSummary(projectId);

        return ResponseEntity.ok(summary);
    }

    //위클리
    @GetMapping("/{projectId}/commits/weekly")
    public ResponseEntity<List<WeeklyCommitCountDto>> getWeeklyCommits(
            @PathVariable Long projectId,
            @AuthenticationPrincipal CustomUserPrincipal principal){
        return ResponseEntity.ok(commitService.getWeeklyCommits(projectId, principal.getUser()));
    }

    //히스토리
    @GetMapping("/{projectId}/commits/history")
    public ResponseEntity<?> getCommitHistory(@PathVariable Long projectId,
                                              @AuthenticationPrincipal CustomUserPrincipal principal){
        return ResponseEntity.ok(commitService.getCommitHistory(projectId, principal.getUser()));
    }

    //전체 커밋 조회
    @GetMapping("/{projectId}/commits")
    public ResponseEntity<List<CommitResponseDto>> getAllCommits( @PathVariable Long projectId,
                                                  @AuthenticationPrincipal CustomUserPrincipal principal){
        return ResponseEntity.ok(commitService.getAllCommit(projectId, principal.getUser()));
    }
}
