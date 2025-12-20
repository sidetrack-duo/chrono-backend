package com.chrono.client;

import com.chrono.dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    public List<WeeklyCommitCountDto> analyzeWeekly(
            WeeklyAnalyzeRequestDto request
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<WeeklyAnalyzeRequestDto> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<WeeklyCommitCountDto[]> response =
                restTemplate.postForEntity(
                        "http://localhost:8000/analyze/weekly",
                        entity,
                        WeeklyCommitCountDto[].class
                );

        return List.of(response.getBody());
    }

    public List<CommitHistoryCountDto> analyzeHistory(HistoryAnalyzeRequestDto requestDto){
        HttpHeaders  headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HistoryAnalyzeRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<CommitHistoryCountDto[]> response = restTemplate.postForEntity(
                "http://localhost:8000/analyze/history",
                entity,
                CommitHistoryCountDto[].class
        );
        return List.of(response.getBody());
    }

}
