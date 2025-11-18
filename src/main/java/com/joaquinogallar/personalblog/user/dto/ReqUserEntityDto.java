package com.joaquinogallar.personalblog.user.dto;

public record ReqUserEntityDto(
   String username,
   String email,
   String password
) {}
