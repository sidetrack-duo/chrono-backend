package com.chrono.service;

import com.chrono.client.PythonCommitAnalyzerClient;
import com.chrono.dto.*;
import com.chrono.entity.CommitEntity;
import com.chrono.entity.ProjectEntity;
import com.chrono.entity.UserEntity;
import com.chrono.mapper.CommitMapper;
import com.chrono.repository.CommitRepository;
import com.chrono.repository.ProjectRepository;
import com.chrono.util.CryptoUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommitService {
    private final ProjectRepository projectRepository;
    private final CommitRepository commitRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private final CryptoUtil cryptoUtil;
    private final CommitMapper commitMapper;
    private final PythonCommitAnalyzerClient pythonCommitAnalyzerClient;

    //커밋 동기화
    @Transactional
    public int syncCommits(Long projectId){
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(()->new EntityNotFoundException("프로젝트가 없음"));

        String url = "https://api.github.com/repos/" +
                project.getOwner() + "/" + project.getRepoName() + "/commits";

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "chrono");

        //pat
        String encryptedPat = project.getUser().getGithubPat();

        if(project.getUser().getGithubPat() != null){
            String decryptedPat = decryptPat(encryptedPat);

            headers.set("Authorization", "token " + decryptedPat);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GithubCommitDto[]> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, GithubCommitDto[].class);

        GithubCommitDto[] commits = response.getBody();

        int savedCount = 0;
        for(GithubCommitDto dto : commits){
            if(commitRepository.existsByProject_ProjectIdAndSha(projectId, dto.getSha())){
                continue;
            }

            CommitEntity commit = CommitEntity.builder()
                    .project(project)
                    .sha(dto.getSha())
                    .message(dto.getCommit().getMessage())
                    .authorName(dto.getCommit().getAuthor().getName())
                    .authorEmail(dto.getCommit().getAuthor().getEmail())
                    .commitDate(dto.getCommit().getAuthor().getDate())
                    .build();

            commitRepository.save(commit);

            savedCount++;
        }

        updateProjectCommitStats(projectId);

        return savedCount;
    }

    private String decryptPat(String encryptedPat){
        return cryptoUtil.decrypt(encryptedPat);
    }

    //커밋 개수 조회
    public int getCommitCount(Long projectId){
        if(!projectRepository.existsById(projectId)){
            throw new EntityNotFoundException("프로젝트 없음");
        }
        return commitRepository.countByProject_ProjectId(projectId);
    }

    //최근 커밋 날짜 조회
    public LocalDateTime getLatestCommitDate(Long projectId){
        LocalDateTime latest = commitMapper.findLatestCommitDate(projectId);

        if(latest == null){
            throw new EntityNotFoundException("커밋이 존재하지 않습니다.");
        }
            return latest;
    }

    //커밋 통계
    public CommitSummaryDto getCommitSummary(Long projectId){

        var commits = commitMapper.findCommitsForAnalysis(projectId);

        var request = new CommitAnalyzeRequestDto(projectId, commits);

        var result = pythonCommitAnalyzerClient.analyzeSummary(request);

        return new CommitSummaryDto(projectId,
                result.total(),
                result.latest(),
                result.weekly(),
                result.mostActiveDay());
    }

    //프로젝트 커밋 통계 반영
    @Transactional
    public void updateProjectCommitStats(Long projectId){
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트 없음"));
        int total = fetchTotalCommitCountFromGithub(project);
        LocalDateTime latest = commitMapper.findLatestCommitDate(projectId);

        project.updateCommitStats(total, latest);
    }

    //총 커밋 수 불러오기
    private int fetchTotalCommitCountFromGithub(ProjectEntity project) {

        //pat연동 안되어 있는 경우
        if(project.getUser().getGithubPat() == null){
            return commitRepository.countByProject_ProjectId(project.getProjectId());
        }

        String url = "https://api.github.com/graphql";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + decryptPat(project.getUser().getGithubPat()));
        headers.set("User-Agent", "chrono");

        Map<String, String> body = Map.of("query",
                """
                {
                  repository(owner: "%s", name: "%s") {
                    defaultBranchRef {
                      target {
                        ... on Commit {
                          history {
                            totalCount
                          }
                        }
                      }
                    }
                  }
                }
                """.formatted(project.getOwner(), project.getRepoName())
        );

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                url,
                entity,
                Map.class
        );

        Map data = (Map) response.getBody().get("data");
        Map repo = (Map) data.get("repository");
        Map ref = (Map) repo.get("defaultBranchRef");
        Map target = (Map) ref.get("target");
        Map history = (Map) target.get("history");

        return (Integer) history.get("totalCount");
    }

    //위클리
    public List<WeeklyCommitCountDto> getWeeklyCommits(Long projectId, UserEntity user){
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트 없음"));

        if (!project.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("권한 없음");
        }

        List<String> commitDates = commitMapper.findCommitDatesForAnalysis(projectId);

        WeeklyAnalyzeRequestDto request =
                new WeeklyAnalyzeRequestDto(projectId, commitDates);

        return pythonCommitAnalyzerClient.analyzeWeekly(request);
    }

    //히스토리
    public List<CommitHistoryCountDto> getCommitHistory(Long projectId, UserEntity user){
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트 없음"));

        if (!project.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("권한 없음");
        }

        if(!project.isActive()){
            throw new EntityNotFoundException("삭제된 프로젝트");
        }
        return commitMapper.selectDailyCommitHistory(projectId);
    }

    //커밋 전체 조회
    @Transactional(readOnly = true)
    public List<CommitResponseDto> getAllCommit(Long projectId, UserEntity user) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트 없음"));

        if (!project.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("권한 없음");
        }

        if (!project.isActive()) {
            throw new EntityNotFoundException("삭제된 프로젝트");
        }

        return commitMapper.findAllCommitsByProject(projectId);
    }
}
