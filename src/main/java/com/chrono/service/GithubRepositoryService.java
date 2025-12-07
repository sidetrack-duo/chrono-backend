package com.chrono.service;

import com.chrono.dto.GithubRepoDto;
import com.chrono.entity.UserEntity;
import com.chrono.enums.GithubConnectStatus;
import com.chrono.repository.UserRepository;
import com.chrono.util.CryptoUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubRepositoryService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CryptoUtil cryptoUtil;

    private static final String PUBLIC_REPOS_URL =
            "https://api.github.com/users/%s/repos?per_page=100";

    private static final String PRIVATE_REPOS_URL =
            "https://api.github.com/user/repos?per_page=100";

    //통합 레포 조회
    public List<GithubRepoDto> getRepositories(UserEntity user){
        GithubConnectStatus status = user.getGithubConnected();

        if(status == GithubConnectStatus.NONE){
            throw new RuntimeException("github연동이 필요");
        }

        if(status == GithubConnectStatus.BASIC){
            return fetchPublicRepos(user.getGithubUsername());
        }

        if(status == GithubConnectStatus.FULL){
            return fetchPrivateAndPublicRepos(user);
        }
        throw new RuntimeException("github 연동 실패");
    }

    //퍼블릭 레포 조회
    private List<GithubRepoDto> fetchPublicRepos(String username){
        try{
            String url = String.format(PUBLIC_REPOS_URL, username);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());

            List<GithubRepoDto> result = new ArrayList<>();
            for(JsonNode repo : root){
                result.add(mapToRepoDto(repo));
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("github 퍼블릭 레포 조회 실패");
        }
    }

    //프라이빗 레포 조회
    private List<GithubRepoDto> fetchPrivateAndPublicRepos(UserEntity user){
        try {
            String pat = cryptoUtil.decrypt(user.getGithubPat());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + pat);
            headers.set("Accept", "application/vnd.github+json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(PRIVATE_REPOS_URL, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());

            List<GithubRepoDto> result = new ArrayList<>();
            for(JsonNode repo : root){
                result.add(mapToRepoDto(repo));
            }
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("github 프라이빗 레포 조회 실패");
        }
    }

    //json > dto
    private GithubRepoDto mapToRepoDto(JsonNode node){
        return GithubRepoDto.builder()
                .repoId(node.get("id").asLong())
                .repoName(node.get("name").asText())
                .fullName(node.get("full_name").asText())
                .description(node.get("description").isNull() ? null : node.get("description").asText())
                .isPrivate(node.get("private").asBoolean())
                .htmlUrl(node.get("html_url").asText())
                .language(node.get("language").isNull() ? null : node.get("language").asText())
                .stargazersCount(node.get("stargazers_count").asInt())
                .forksCount(node.get("forks_count").asInt())
                .updatedAt(node.get("updated_at").asText())
                .build();
    }
}
