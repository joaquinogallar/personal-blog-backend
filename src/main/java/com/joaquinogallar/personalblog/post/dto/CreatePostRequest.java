package com.joaquinogallar.personalblog.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreatePostRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100)
        String title,

        @NotBlank(message = "Content is required")
        String content,
        Boolean published,
        Set<Long> tagIds
) {}
