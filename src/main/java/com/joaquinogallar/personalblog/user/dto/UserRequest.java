package com.joaquinogallar.personalblog.user.dto;

public record UserRequest(
   String username,
   String email,
   String password
) {}
