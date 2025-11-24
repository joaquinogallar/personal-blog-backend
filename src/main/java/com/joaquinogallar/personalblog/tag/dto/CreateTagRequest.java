package com.joaquinogallar.personalblog.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTagRequest(
        @NotBlank(message = "Name tag is required")
        @Size(max = 50)
        String name,

        @NotBlank(message = "Slug is required")
        @Size(max = 40)
        String slug
) {}