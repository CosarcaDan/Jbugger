package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.dto.PermissionDto;
import ro.msg.edu.jbugs.dto.mappers.PermissionDtoMapping;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.repo.PermissionRepo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

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

    public void addPermission(PermissionDto permissionDto) {
        Permission permission = PermissionDtoMapping.permissionDtoToPermission(permissionDto);
        permission.setId(null);
        permissionRepo.addPermission(permission);
    }

    public PermissionDto findPermission(Integer id) {
        Permission permission = permissionRepo.findPermission(id);
        PermissionDto permissionDto = PermissionDtoMapping.permissionToPermissionDto(permission);
        return permissionDto;
    }
}
