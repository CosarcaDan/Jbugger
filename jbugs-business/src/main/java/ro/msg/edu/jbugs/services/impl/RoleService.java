package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.repo.RoleRepo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

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
//
//    public void addRole(RoleDto roleDto){
//        Role role = RoleDtoMapping.roleDtoToRole(roleDto);
//        roleRepo.addRole(role);
//    }
//
//    public RoleDto findRole(Integer id){
//        Role role = roleRepo.findRole(id);
//        RoleDto roleDto = RoleDtoMapping.roleToroleDto(role);
//        return roleDto;
//    }

//    public void addRole(){
//        Role role = new Role("");
//        roleRepo.addRole(role);
//    }
}
