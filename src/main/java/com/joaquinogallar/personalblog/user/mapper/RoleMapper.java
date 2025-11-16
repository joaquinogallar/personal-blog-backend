package com.joaquinogallar.personalblog.user.mapper;

import com.joaquinogallar.personalblog.user.dto.RoleDto;
import com.joaquinogallar.personalblog.user.entity.Role;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public RoleDto mapToRoleDto(Role role) {
        if (role == null) return null;

        return new RoleDto(
                role.getId(),
                role.getRole()
        );
    }

    public Set<RoleDto> mapToRoleDtoList(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream().map(this::mapToRoleDto).collect(Collectors.toSet());
    }
}
