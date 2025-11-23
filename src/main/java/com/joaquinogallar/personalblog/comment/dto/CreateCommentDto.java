package com.joaquinogallar.personalblog.comment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentDto(
        @NotNull(message = "Post ID is required")
        Long postId,

        @NotBlank(message = "Contenido is required")
        @Size(min = 10, max = 2000)
        String content,

        @Size(max = 100)
        String authorName,

        @Email(message = "Invalid email format")
        String authorEmail
) {}
