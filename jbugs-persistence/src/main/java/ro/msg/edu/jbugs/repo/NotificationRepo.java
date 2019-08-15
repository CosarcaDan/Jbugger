package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Notification;
import ro.msg.edu.jbugs.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Stateless
public class NotificationRepo {
    @PersistenceContext(unitName = "jbugs-persistence")
    EntityManager entityManager;

    public Notification addNotification(Notification notification){
        entityManager.persist(notification);
        entityManager.flush();
        return notification;
    }

    public Notification findNotification(Integer id){
        return entityManager.find(Notification.class,id);
    }

    public Integer deleteNotificationAfterUserId(User user){
        Query query = entityManager.createNamedQuery(Notification.DELETE_NOTIFICATION_AFTER_USER_ID);
        query.setParameter("user",user);
        Integer result = query.executeUpdate();
        return result;
    }
}
