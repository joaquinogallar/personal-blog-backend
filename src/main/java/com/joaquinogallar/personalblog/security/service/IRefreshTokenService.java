package com.joaquinogallar.personalblog.security.service;

import com.joaquinogallar.personalblog.security.entity.RefreshToken;
import com.joaquinogallar.personalblog.user.entity.User;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken validateRefreshToken(String token);
    void revokeRefreshToken(String token);
}
