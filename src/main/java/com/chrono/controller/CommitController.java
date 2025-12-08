package com.chrono.controller;

import com.chrono.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
