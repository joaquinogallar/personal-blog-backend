package com.joaquinogallar.personalblog.user.dto;

import com.joaquinogallar.personalblog.user.entity.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        LocalDateTime createdAt,
        Set<Role> roles
) {
}
