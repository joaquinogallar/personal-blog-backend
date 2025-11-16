package com.joaquinogallar.personalblog.user.mapper;

import com.joaquinogallar.personalblog.user.dto.UserDto;
import com.joaquinogallar.personalblog.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    private final RoleMapper roleMapper;

    public UserEntityMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserDto toDto(UserEntity user) {
        if (user == null) return null;

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreateAt(),
                roleMapper.mapToRoleDtoList(user.getRoles())
        );
    }

}
