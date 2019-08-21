package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.dto.PermissionDto;
import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.dto.mappers.PermissionDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.RoleDtoMapping;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.repo.PermissionRepo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Stateless
public class PermissionService {
    @EJB
    private PermissionRepo permissionRepo;

    public void addPermission(PermissionDto permissionDto){
        Permission permission = PermissionDtoMapping.permissionDtoToPermission(permissionDto);
        permissionRepo.addPermission(permission);
    }

    public PermissionDto findPermission(Integer id){
        Permission permission = permissionRepo.findPermission(id);
        PermissionDto permissionDto = PermissionDtoMapping.permissionToPermissionDto(permission);
        return permissionDto;
    }

    public List<PermissionDto> getAllPermissions()
    {
        return permissionRepo.getAllPermissions().stream().map(PermissionDtoMapping::permissionToPermissionDto).collect(Collectors.toList());

    }

    public List<PermissionDto> getPermissionsNotInRole(RoleDto role) {
        return permissionRepo.getPermissionsNotInRole(RoleDtoMapping.roleDtoToRole(role)).stream().map(PermissionDtoMapping::permissionToPermissionDto).collect(Collectors.toList());
    }

    public List<PermissionDto> getPermissionsInRole(RoleDto role) {
        return permissionRepo.getPermissionsInRole(RoleDtoMapping.roleDtoToRole(role)).stream().map(PermissionDtoMapping::permissionToPermissionDto).collect(Collectors.toList());
    }
}
