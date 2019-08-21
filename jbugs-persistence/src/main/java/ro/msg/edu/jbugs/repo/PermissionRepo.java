package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.entity.Role;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Stateless
public class PermissionRepo {
    @PersistenceContext(unitName = "jbugs-persistence")
    EntityManager entityManager;

    public List<Permission> getAllPermissions() {
        TypedQuery<Permission> query = entityManager.createNamedQuery(Permission.GET_ALL_PERMISSIONS, Permission.class);
        return query.getResultList();
    }

    public Permission addPermission(Permission permission){
        entityManager.persist(permission);
        entityManager.flush();
        return permission;
    }

    public Permission findPermission(Integer id){
        return entityManager.find(Permission.class,id);
    }

    public List<Permission> getPermissionsNotInRole(Role role) {
        TypedQuery<Permission> query = entityManager.createNamedQuery(Permission.GET_ALL_PERMISSIONS, Permission.class);
        return query.getResultList().stream().filter(permission -> !(permission.getRoleList().size()!=0 && permission.getRoleList().stream().anyMatch(role1 -> role1.getId().equals(role.getId())))).collect(Collectors.toList());
    }
}
