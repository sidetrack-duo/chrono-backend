package com.chrono.service;

import com.chrono.dto.CommitSummaryDto;
import com.chrono.dto.GithubCommitDto;
import com.chrono.entity.CommitEntity;
import com.chrono.entity.ProjectEntity;
import com.chrono.mapper.CommitMapper;
import com.chrono.repository.CommitRepository;
import com.chrono.repository.ProjectRepository;
import com.chrono.util.CryptoUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommitService {
    private final ProjectRepository projectRepository;
    private final CommitRepository commitRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private final CryptoUtil cryptoUtil;
    private final CommitMapper commitMapper;

    //커밋 동기화
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

        int total = commitMapper.countTotalCommits(projectId);
        LocalDateTime  latest = commitMapper.findLatestCommitDate(projectId);
        int weekly = commitMapper.countCommitsThisWeek(projectId);
        String mostActiveDay = commitMapper.findMostActiveDay(projectId);

        return new CommitSummaryDto(
                projectId,
                total,
                latest,
                weekly,
                mostActiveDay
        );
    }
}
