package com.joaquinogallar.personalblog.comment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentDto(
        Long id,
        String authorName,
        String authorEmail,
        // TODO: should I add @Lob here?
        String content,
        LocalDateTime createdAt,
        Long postId,
        UUID userId
) {
}
