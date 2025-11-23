package com.joaquinogallar.personalblog.post.dto;

import com.joaquinogallar.personalblog.tag.dto.TagResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record PostResponse(
        Long id,
        String title,
        String content,
        String slug,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean published,
        UUID userId,
        Set<TagResponse> tags
) {
}
