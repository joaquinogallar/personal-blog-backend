package com.joaquinogallar.personalblog.user.dto;

import jakarta.validation.constraints.*;

public record UserRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20)
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Bad email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 20)
        String password
) {}
