package com.chrono.client;

import com.chrono.dto.CommitAnalyzeRequestDto;
import com.chrono.dto.CommitAnalyzeResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PythonCommitAnalyzerClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String PYTHON_URL = "http://localhost:8000/analyze/summary";

    public CommitAnalyzeResponseDto analyzeSummary(CommitAnalyzeRequestDto requestDto){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CommitAnalyzeRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<CommitAnalyzeResponseDto> response =
                restTemplate.postForEntity(PYTHON_URL, entity, CommitAnalyzeResponseDto.class);

        return response.getBody();
    }
}
