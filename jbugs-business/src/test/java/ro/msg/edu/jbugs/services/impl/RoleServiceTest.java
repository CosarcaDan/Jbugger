package ro.msg.edu.jbugs.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.msg.edu.jbugs.dto.PermissionDto;
import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.entity.Role;
import ro.msg.edu.jbugs.repo.PermissionRepo;
import ro.msg.edu.jbugs.repo.RoleRepo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PermissionRepo permissionRepo;

    public RoleServiceTest(){
        this.roleService = new RoleService();
    }

    @Test
    public void addRole() {
        when(roleRepo.addRole(any(Role.class))).thenReturn(new Role());
        this.roleService.addRole(new RoleDto());
    }

    @Test
    public void findRole() {
        Role returnRole = new Role();
        returnRole.setId(1);
        returnRole.setType("Admin");
        returnRole.setPermissionList(new ArrayList<>());
        returnRole.setUserList(new ArrayList<>());
        when(roleRepo.findRole(1)).thenReturn(returnRole);
        RoleDto role = roleService.findRole(1);
        assertEquals(1,(long)role.getId());
        assertEquals("Admin",role.getType());
    }

    @Test
    public void addPermissionToRole() {
        when(permissionRepo.findPermission(any(Integer.class))).thenReturn(new Permission());
        when(roleRepo.addPermissionToRole(any(Role.class),any(Permission.class))).thenReturn(new Role());
        roleService.addPermissionToRole(new RoleDto(),new PermissionDto());
    }

    @Test
    public void getAllRoles() {
        when(roleRepo.getAllRoles()).thenReturn(new ArrayList<>());
        assertEquals(0, (long) roleService.getAllRoles().size());
    }

    @Test
    public void getPermissionsByRole() {
        when(roleRepo.getPermissionsByRole(any(Role.class))).thenReturn(new ArrayList<>());
        RoleDto rdto = new RoleDto();
        rdto.setId(1);
        List<PermissionDto> permissionsByRole = roleService.getPermissionsByRole(rdto);
        assertEquals(0,permissionsByRole.size());

    }

    @Test
    public void removePermissionToRole() {
        when(permissionRepo.findPermission(anyInt())).thenReturn(new Permission());
        when(roleRepo.removePermissionFromRole(any(Role.class),any(Permission.class))).thenReturn(null);
        roleService.removePermissionFromRole(new RoleDto(),new PermissionDto());
    }
}