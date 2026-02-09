package com.chrono.exception;

import com.chrono.dto.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException e){
        log.debug("bad_request:{}",e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponseDto.of(e.getMessage(), "bad_request"));
    }

    //404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(
            EntityNotFoundException e) {
        log.debug("not_found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.of(e.getMessage(),"not_found"));
    }

    //403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException e){
        log.debug("access denied: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponseDto.of(e.getMessage(), "access_denied"));
    }

    //500
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntime(RuntimeException e){
        log.error("server error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.of(e.getMessage(), "internal server error"));
    }

    //그 외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error("unexpected error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseDto.of(
                        "서버 오류가 발생했습니다.",
                        "unexpected_error"
                ));
    }
}
