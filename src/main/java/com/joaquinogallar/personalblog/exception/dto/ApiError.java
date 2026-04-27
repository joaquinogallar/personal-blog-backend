package com.joaquinogallar.personalblog.exception.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime time
) {
    public static ApiError of(HttpStatus status, String message, String path) {
        return new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                LocalDateTime.now()
        );
    }
}
