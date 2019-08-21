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

    public void addRole(RoleDto roleDto) {
        Role role = RoleDtoMapping.roleDtoToRole(roleDto);
        role.setId(null);
        roleRepo.addRole(role);
    }

    public RoleDto findRole(Integer id) {
        Role role = roleRepo.findRole(id);
        RoleDto roleDto = RoleDtoMapping.roleToRoleDto(role);
        return roleDto;
    }

    public void addPermissionToRole(RoleDto roleDto, PermissionDto permissionDto) {
        Permission permission = permissionRepo.findPermission(permissionDto.getId());
        roleRepo.addPermissionToRole(RoleDtoMapping.roleDtoToRole(roleDto), permission);
    }

    public List<RoleDto> getAllRoles() {
        return roleRepo.getAllRoles().stream().map(RoleDtoMapping::roleToRoleDto).collect(Collectors.toList());
    }

    public List<PermissionDto> getPermissionsByRole(RoleDto roleDto) {
        return roleRepo.getPermissionsByRole(RoleDtoMapping.roleDtoToRole(roleDto))
                .stream().map(PermissionDtoMapping::permissionToPermissionDto).collect(Collectors.toList());
    }

    public void removePermissionToRole(RoleDto roleDto, PermissionDto permissionDto) {
        Permission permission = permissionRepo.findPermission(permissionDto.getId());
        roleRepo.removePermissionToRole(RoleDtoMapping.roleDtoToRole(roleDto), permission);
    }

//    public void addRole(){
//        Role role = new Role("");
//        roleRepo.addRole(role);
//    }
}
