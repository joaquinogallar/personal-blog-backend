package com.joaquinogallar.personalblog.user.repository;

import com.joaquinogallar.personalblog.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByEmail(String email);
    User findUserByUsername(String username);
}
