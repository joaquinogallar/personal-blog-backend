package com.joaquinogallar.personalblog.user.mapper;

import com.joaquinogallar.personalblog.user.dto.UserResponse;
import com.joaquinogallar.personalblog.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse mapUserToDto(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreateAt(),
                user.getRoles()
        );
    }

    public List<UserResponse> mapUserToDto(List<User> users) {
        if(users == null) return null;
        return users.stream().map(this::mapUserToDto).collect(Collectors.toList());
    }

}
