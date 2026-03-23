package com.joaquinogallar.personalblog.security.service;

import com.joaquinogallar.personalblog.security.entity.CustomUserDetails;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(usernameOrEmail)
                .or(() -> userRepository.findUserByEmail(usernameOrEmail))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found")
                );

        return new CustomUserDetails(user);
    }
}
