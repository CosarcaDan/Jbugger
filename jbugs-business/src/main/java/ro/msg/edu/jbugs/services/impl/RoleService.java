package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.dto.PermissionDto;
import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.dto.mappers.PermissionDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.RoleDtoMapping;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.entity.Role;
import ro.msg.edu.jbugs.repo.PermissionRepo;
import ro.msg.edu.jbugs.repo.RoleRepo;

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
public class RoleService {

    @EJB
    private RoleRepo roleRepo;
    @EJB
    private PermissionRepo permissionRepo;


    /**
     * This function adds a role to the repository
     * @param roleDto the DTO to be added
     */
    public void addRole(RoleDto roleDto) {
        Role role = RoleDtoMapping.roleDtoToRole(roleDto);
        role.setId(null);
        roleRepo.addRole(role);
    }

    /**
     * Find a role with a given id
     * @param id
     * @return returns the role with the given id.
     */
    public RoleDto findRole(Integer id) {
        Role role = roleRepo.findRole(id);
        return RoleDtoMapping.roleToRoleDto(role);
    }

    /**
     * Adds a permission to the role
     * @param roleDto the DTO of the role that the permission is added to
     * @param permissionDto the DTO that is going to be added to the role
     */
    public void addPermissionToRole(RoleDto roleDto, PermissionDto permissionDto) {
        Permission permission = permissionRepo.findPermission(permissionDto.getId());
        roleRepo.addPermissionToRole(RoleDtoMapping.roleDtoToRole(roleDto), permission);
    }

    /**
     * Gat a list of all roles
     * @return RoleDto list of all roles in the repository
     */
    public List<RoleDto> getAllRoles() {
        return roleRepo.getAllRoles().stream().map(RoleDtoMapping::roleToRoleDto).collect(Collectors.toList());
    }

    /**
     * Get the  all permissions of a role
     * @param roleDto The DTO of the role of which the permissions are returned
     * @return A list of all permissions from the given role
     */
    public List<PermissionDto> getPermissionsByRole(RoleDto roleDto) {
        return roleRepo.getPermissionsByRole(RoleDtoMapping.roleDtoToRole(roleDto))
                .stream().map(PermissionDtoMapping::permissionToPermissionDto).collect(Collectors.toList());
    }

    /**
     * Removes the given permission from the given role
     * @param roleDto the DTO of the role that the permission is removed from
     * @param permissionDto the DTO that is going to be removed from the role
     */
    public void removePermissionFromRole(RoleDto roleDto, PermissionDto permissionDto) {
        Permission permission = permissionRepo.findPermission(permissionDto.getId());
        roleRepo.removePermissionFromRole(RoleDtoMapping.roleDtoToRole(roleDto), permission);
    }


//    public void addRole(){
//        Role role = new Role("");
//        roleRepo.addRole(role);
//    }
}
