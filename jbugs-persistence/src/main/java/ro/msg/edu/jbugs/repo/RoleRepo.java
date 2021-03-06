package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.entity.Role;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Stateless
public class RoleRepo {
    @PersistenceContext(unitName = "jbugs-persistence")
    EntityManager entityManager;

    public Role addRole(Role role) {
        entityManager.persist(role);
        entityManager.flush();
        return role;
    }

    public Role findRole(Integer id) {
        return entityManager.find(Role.class, id);
    }


    public Role addPermissionToRole(Role roleinp, Permission permission) {
        Role role = entityManager.find(Role.class, roleinp.getId());
        role.addPermission(permission);
        entityManager.merge(role);
        entityManager.flush();
        return role;
    }

    public List<Role> getAllRoles() {
        TypedQuery<Role> query = entityManager.createNamedQuery(Role.GET_ALL_ROLES, Role.class);
        return query.getResultList();
    }

    public List<Permission> getPermissionsByRole(Role role) {
//        TypedQuery<Permission> query = entityManager.createNamedQuery(Role.GET_PERMISSIONS_BY_ROLE, Permission.class);
//        query.setParameter("id",role.getId());
//        return query.getResultList();
        role = entityManager.find(Role.class, role.getId());
        return role.getPermissionList();


    }

    public Role removePermissionFromRole(Role roleinp, Permission permission) {
        Role role = entityManager.find(Role.class, roleinp.getId());
        role.removePermission(permission);
        entityManager.merge(role);
        entityManager.flush();
        return role;
    }

    public Role findAdminRole() {
        for (Role role : getAllRoles()) {
            if (role.getType().equals("Administrator")) {
                return role;
            }
        }
        return null;
    }

}
