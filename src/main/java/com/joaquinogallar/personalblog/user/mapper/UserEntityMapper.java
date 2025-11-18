package com.joaquinogallar.personalblog.user.mapper;

import com.joaquinogallar.personalblog.user.dto.UserEntityDto;
import com.joaquinogallar.personalblog.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserEntityMapper {

    private final RoleMapper roleMapper;

    public UserEntityMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserEntityDto mapUsertoDto(UserEntity user) {
        if (user == null) return null;

        return new UserEntityDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreateAt(),
                roleMapper.mapToRoleDtoList(user.getRoles())
        );
    }

    public List<UserEntityDto> mapUserToDto(List<UserEntity> users) {
        if(users == null) return null;
        return users.stream().map(this::mapUsertoDto).collect(Collectors.toList());
    }

}
