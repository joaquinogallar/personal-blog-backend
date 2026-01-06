package com.joaquinogallar.personalblog.user.util;

import com.joaquinogallar.personalblog.user.entity.Role;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile("local")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String admUser;
    @Value("${app.admin.email}")
    private String admEmail;
    @Value("${app.admin.password}")
    private String admPass;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if(userRepository.findUserByUsername(admUser).isEmpty()) {
            User user = User.builder()
                    .username(admUser)
                    .email(admEmail)
                    .passwordHash(passwordEncoder.encode(admPass))
                    .roles(Set.of(Role.USER, Role.ADMIN))
                    .build();

            userRepository.save(user);
        }


    }
}
