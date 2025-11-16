package com.joaquinogallar.personalblog.user.mapper;

import com.joaquinogallar.personalblog.user.dto.RoleDto;
import com.joaquinogallar.personalblog.user.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDto mapToRoleDto(Role role) {
        if (role == null) return null;

        return new RoleDto(
                role.getId(),
                role.getRole()
        );
    }

}
