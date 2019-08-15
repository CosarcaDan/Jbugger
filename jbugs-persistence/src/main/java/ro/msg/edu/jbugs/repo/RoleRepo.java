package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Role;

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


}
