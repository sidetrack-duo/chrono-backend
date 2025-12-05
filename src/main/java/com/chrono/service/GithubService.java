package com.chrono.service;

import com.chrono.dto.ValidationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GithubService {
    private final RestTemplate restTemplate = new RestTemplate();

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
}
