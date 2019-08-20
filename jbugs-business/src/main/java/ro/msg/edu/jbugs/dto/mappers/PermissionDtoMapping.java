package ro.msg.edu.jbugs.dto.mappers;

import ro.msg.edu.jbugs.dto.PermissionDto;
import ro.msg.edu.jbugs.entity.Permission;

public class PermissionDtoMapping {
    public static Permission permissionDtoToPermission(PermissionDto permissionDto) {
        Permission result = new Permission();
        result.setId(permissionDto.getId());
        result.setDescription(permissionDto.getDescription());
        result.setType(permissionDto.getType());
        return result;
    }

    public static PermissionDto permissionToPermissionDto(Permission permission) {
        PermissionDto result = new PermissionDto();
        result.setId(permission.getId());
        result.setDescription(permission.getDescription());
        result.setType(permission.getType());
        return result;
    }
}
