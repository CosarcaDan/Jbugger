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

    public Role addRole(Role role){
        entityManager.persist(role);
        entityManager.flush();
        return role;
    }

    public Role findRole(Integer id){
        return entityManager.find(Role.class,id);
    }


    public void addPermissionToRole(Role roleimp, Permission permission) {
        Role role = entityManager.find(Role.class, roleimp.getId());
        role.addPermission(permission);
        entityManager.merge(role);
        entityManager.flush();
    }
    public List<Role> getAllRoles()
    {
        TypedQuery<Role> query = entityManager.createNamedQuery(Role.GET_ALL_ROLES, Role.class);
        return query.getResultList();
    }

    public List<Permission> getPermissionsByRole(Role role)
    {
        TypedQuery<Permission> query = entityManager.createNamedQuery(Role.GET_PERMISSIONS_BY_ROLE, Permission.class);
        query.setParameter("id",role.getId());
        return query.getResultList();
    }
}
