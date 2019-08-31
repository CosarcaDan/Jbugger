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

    /**
     * Adds a permission to the repository
     * @param permissionDto The permission that is going tp be added
     */
    public void addPermission(PermissionDto permissionDto) {
        Permission permission = PermissionDtoMapping.permissionDtoToPermission(permissionDto);
        permissionRepo.addPermission(permission);
    }

    /**
     * Returns the permission with the given id
     * @param id The Id of the permission that is going to be returned
     * @return The permission in DTO form 1ith the given id
     */
    public PermissionDto findPermission(Integer id) {
        Permission permission = permissionRepo.findPermission(id);
        PermissionDto permissionDto = PermissionDtoMapping.permissionToPermissionDto(permission);
        return permissionDto;
    }

    /**
     * @return A list of all permissions
     */
    public List<PermissionDto> getAllPermissions() {
        return permissionRepo.getAllPermissions().stream().map(PermissionDtoMapping::permissionToPermissionDto).collect(Collectors.toList());

    }


    /**
     * Return all the permission that are not in the given role
     * @param role the role that is going to be searched
     * @return A list of all the permission not in the given role
     */
    public List<PermissionDto> getPermissionsNotInRole(RoleDto role) {
        return permissionRepo.getPermissionsNotInRole(RoleDtoMapping.roleDtoToRole(role)).stream().map(PermissionDtoMapping::permissionToPermissionDto).collect(Collectors.toList());
    }

    /**
     * Return all the permission that are in the given role
     * @param role the role that is going to be searched
     * @return A list of all the permission in the given role
     */
    public List<PermissionDto> getPermissionsInRole(RoleDto role) {
        return permissionRepo.getPermissionsInRole(RoleDtoMapping.roleDtoToRole(role)).stream().map(PermissionDtoMapping::permissionToPermissionDto).collect(Collectors.toList());
    }
}
