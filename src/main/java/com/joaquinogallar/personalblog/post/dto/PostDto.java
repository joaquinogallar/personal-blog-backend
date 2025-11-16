package com.joaquinogallar.personalblog.post.dto;

import com.joaquinogallar.personalblog.comment.dto.CommentDto;
import com.joaquinogallar.personalblog.tag.dto.TagDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record PostDto(
        Long id,
        String title,
        String content,
        String slug,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean published,
        UUID userId,
        Set<TagDto> tags,
        List<CommentDto> comments
) {
}
