package com.chrono.service;

import com.chrono.dto.*;
import com.chrono.entity.ProjectEntity;
import com.chrono.entity.UserEntity;
import com.chrono.repository.ProjectRepository;
import com.chrono.repository.UserRepository;
import com.chrono.util.CryptoUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final CryptoUtil cryptoUtil;


    public Long createProject(UserEntity user, CreateProjectRequestDto req) {

        //중복 등록 체크
        boolean duplicated = projectRepository
                .existsByUser_UserIdAndOwnerAndRepoName(
                        user.getUserId(),
                        req.getOwner(),
                        req.getRepoName()
                );
        if (duplicated) {
            throw new IllegalArgumentException("이미 등록된 프로젝트 입니다.");
        }

        //레포 존재여부 검증
        validateGithubRepoExists(req.getOwner(), req.getRepoName(), user);

        //새 플젝 엔티티 생성
        ProjectEntity project = ProjectEntity.builder()
                .user(user)
                .owner(req.getOwner())
                .repoName(req.getRepoName())
                .repoUrl(req.getRepoUrl())
                .title(req.getTitle())
                .description(req.getDescription())
                .techStack(req.getTechStack())
                .startDate(req.getStartDate())
                .targetDate(req.getTargetDate())
                .build();
        //저장
        ProjectEntity saved = projectRepository.save(project);

        return saved.getProjectId();
    }

    //git repo검증 메서드
    private void validateGithubRepoExists(String owner, String repoName, UserEntity user){
        String url = "https://api.github.com/repos/" + owner + "/" + repoName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "chrono");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return; // public repo 정상 접근됨 → 바로 return
        }
        catch (HttpClientErrorException.Forbidden e) {

        }
        catch (HttpClientErrorException.NotFound e) {

        }

        if (user.getGithubPat() == null) {
            throw new IllegalArgumentException("private repo 입니다. pat 등록이 필요합니다.");
        }

        validateGithubRepoWithPat(owner, repoName, user);
    }

    private void validateGithubRepoWithPat(String owner, String repoName, UserEntity user) {
        String url = "https://api.github.com/repos/" + owner + "/" + repoName;
        String pat = decryptPat(user.getGithubPat());

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "chrono");
        headers.set("Authorization", "token " + pat);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("존재하지 않는 레포입니다.");
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new IllegalArgumentException("PAT 인증 실패(PAT 잘못되었거나 권한 부족).");
        }
    }

    private String decryptPat(String encryptedPat) {
        return cryptoUtil.decrypt(encryptedPat);
    }

    //리스트 전체 조회
    public List<ProjectResponseDto> getProjects(UserEntity user){

        return projectRepository.findAllByUser(user)
                .stream()
                .map(ProjectResponseDto::fromEntity)
                .toList();
    }

    //프로젝트 추가 입력
    @Transactional
    public void updateProjectMeta(Long projectId, UserEntity user, UpdateProjectMetaDto req){
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트 없음"));
        if(!project.getUser().getUserId().equals(user.getUserId())){

            throw new AccessDeniedException("권한 없음");
        }
        project.updateMeta(
                req.getTitle(),
                req.getDescription(),
                req.getTechStack(),
                req.getStartDate(),
                req.getTargetDate()
        );
    }

    //프로젝트 진행 상태 바꾸기
    @Transactional
    public void updateProjectStatus(Long projectId, UserEntity user, UpdateProjectStatusDto req){
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트 없음"));

        if(!project.getUser().getUserId().equals(user.getUserId())){
            throw new AccessDeniedException("권한 없음");
        }
        //상태변경
        switch (req.getStatus()){
            case COMPLETED -> project.markCompleted();
            case IN_PROGRESS -> project.markInProgress();
            default -> throw new IllegalArgumentException("변경할 수 없는 상태입니다.");
        }
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponseDto getProjectDetail(Long projectId, UserEntity user){
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트 없음"));

        if(!project.getUser().getUserId().equals(user.getUserId())){
            throw new AccessDeniedException("권한 없음");
        }
        return new ProjectDetailResponseDto(
                project.getProjectId(),
                project.getOwner(),
                project.getRepoName(),
                project.getRepoUrl(),
                project.getTitle(),
                project.getDescription(),
                project.getTechStack(),
                project.getStartDate(),
                project.getTargetDate(),
                project.getStatus(),
                project.isActive(),
                project.getCreatedAt(),
                project.getTotalCommits(),
                project.getLastCommitAt()
        );
    }
}
