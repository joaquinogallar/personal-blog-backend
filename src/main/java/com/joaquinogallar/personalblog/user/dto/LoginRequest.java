package com.joaquinogallar.personalblog.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotBlank
        @NotEmpty(message = "Username or Email cannot be empty.")
        String usernameOrEmail,
        @NotBlank
        @NotEmpty(message = "Password cannot be empty.")
        String password
) {}
