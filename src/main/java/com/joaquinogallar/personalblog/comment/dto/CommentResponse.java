package com.joaquinogallar.personalblog.comment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        Long id,
        String authorName,
        String authorEmail,
        String content,
        LocalDateTime createdAt,
        Long postId,
        UUID userId
) {}
