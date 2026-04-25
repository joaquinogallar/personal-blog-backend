package com.joaquinogallar.personalblog.security.repository;

import com.joaquinogallar.personalblog.security.entity.RefreshToken;
import com.joaquinogallar.personalblog.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
