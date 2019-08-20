package ro.msg.edu.jbugs.dto.mappers;

import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.entity.Role;

public class RoleDtoMapping {
    public static Role roleDtoToRole(RoleDto roleDto) {
        Role result = new Role();
        result.setId(roleDto.getId());
        result.setType(roleDto.getType());
        return result;
    }

    public static RoleDto roleToRoleDto(Role role) {
        RoleDto result = new RoleDto();
        result.setId(role.getId());
        result.setType(role.getType());
        return result;
    }
}
