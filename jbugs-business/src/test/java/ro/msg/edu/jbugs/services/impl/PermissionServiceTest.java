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

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @InjectMocks
    private PermissionService permissionService;

    @Mock
    private PermissionRepo permissionRepo;

    public PermissionServiceTest(){
        this.permissionService = new PermissionService();
    }

    @Test
    public void addPermission() {
        when(permissionRepo.addPermission(any(Permission.class))).thenReturn(new Permission());
        this.permissionService.addPermission(new PermissionDto());
    }

    @Test
    public void findPermission() {
        Permission returnPermission = new Permission();
        returnPermission.setId(1);
        returnPermission.setDescription("asd");
        when(permissionRepo.findPermission(1)).thenReturn(returnPermission);
        PermissionDto permission = permissionService.findPermission(1);
        assertEquals("asd",permission.getDescription());
        assertEquals(1,(long)permission.getId());
    }

    @Test
    public void getAllPermissions() {
        when(permissionRepo.getAllPermissions()).thenReturn(new ArrayList<>());
        assertEquals(0, (long) permissionService.getAllPermissions().size());
    }

    @Test
    public void getPermissionsNotInPermission() {
        when(permissionRepo.getPermissionsNotInRole(any(Role.class))).thenReturn(new ArrayList<>());
        permissionService.getPermissionsNotInRole(new RoleDto());
    }

    @Test
    public void getPermissionsInPermission() {
        when(permissionRepo.getPermissionsInRole(any(Role.class))).thenReturn(new ArrayList<>());
        permissionService.getPermissionsInRole(new RoleDto());
    }
}