package com.joaquinogallar.personalblog.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTagRequest(
        @NotBlank(message = "Nombre del tag es requerido")
        @Size(max = 50)
        String name
) {}