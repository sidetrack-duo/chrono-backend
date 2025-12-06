package com.chrono.service;

import com.chrono.dto.GithubBasicConnectRequestDto;
import com.chrono.dto.GithubBasicConnectResponseDto;
import com.chrono.dto.ValidationResponseDto;
import com.chrono.entity.UserEntity;
import com.chrono.enums.GithubConnectStatus;
import com.chrono.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GithubService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthService authService;
    private final UserRepository userRepository;

    public ValidationResponseDto validateUsername(String username){
        String url = "https://api.github.com/users/" + username;

        try{
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                //사용자 존재
                return new ValidationResponseDto(true, username,
                        "https://github.com/" + username + ".png", "존재하는 GitHub 사용자입니다."
                );
            }
        }catch (HttpClientErrorException.NotFound e){
            return new ValidationResponseDto(false, username,
                    null, "존재하지 않는 GitHub 사용자입니다.");
        }catch (Exception e){
            // 기타 오류
            return new ValidationResponseDto(false, username,
                    null, "조회 중 오류가 발생했습니다.");
        }
        // 성공 코드가 아니면 존재하지 않음 처리
        return new ValidationResponseDto(false, username,
                null, "존재하지 않는 GitHub 사용자입니다.");
    }

    //깃허브 기본 연동
    public GithubBasicConnectResponseDto connectBasic(String username){
        String url = "https://api.github.com/users/" + username;

        try{
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(response.getBody());

                String avatarUrl = json.get("avatar_url").asText();
                Long githubUserId = json.get("id").asLong();

                UserEntity user = authService.getCurrentUser();

                //full 연동 유저 basic불가
                if(user.getGithubConnected().equals(GithubConnectStatus.FULL)){
                    return new GithubBasicConnectResponseDto(
                            false,
                            "FULL",
                            username,
                            avatarUrl,
                            "이미 PAT 기반 FULL 연동 상태입니다."
                    );
                }

                user.updateGithubBasic(username, avatarUrl, githubUserId);

                userRepository.save(user);

                return new GithubBasicConnectResponseDto(
                        true,
                        "BASIC",
                        username,
                        avatarUrl,
                        "기본 연동이 완료되었습니다."
                );
            }
        }catch (HttpClientErrorException.NotFound e){
            return new GithubBasicConnectResponseDto(
                    false,
                    "NONE",
                    username,
                    null,
                    "존재하지 않는 사용자입니다."
            );
        }catch (Exception e){
            e.printStackTrace();

            return new GithubBasicConnectResponseDto(
                    false,
                    "NONE",
                    username,
                    null,
                    "GitHub 연동 중 오류가 발생했습니다."
            );
        }

        return new GithubBasicConnectResponseDto(
                false,
                "NONE",
                username,
                null,
                "연동 중 예상치 못한 오류가 발생했습니다."
        );
    }
}
