package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Permission;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    public Permission addPermission(Permission permission){
        entityManager.persist(permission);
        entityManager.flush();
        return permission;
    }

    public Permission findPermission(Integer id){
        return entityManager.find(Permission.class,id);
    }
}
