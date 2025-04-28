package ru.otus.ms.core.updates.exception_handlers;


import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.ms.core.updates.exception_handlers.exceptions.NoResourceFoundException;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CustomErrorResource> handleNoResourceFoundException(NoResourceFoundException exception) {
        log.error("Resource not found", exception);
        CustomErrorResource resource = CustomErrorResource.builder()
                .timestamp(Instant.now().toString())
                .error(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(resource);
    }

    @Data
    @Builder
    public static class CustomErrorResource {
        private String timestamp;
        private String error;
    }
}
