package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Notification;
import ro.msg.edu.jbugs.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */


@Stateless
public class NotificationRepo {

    @PersistenceContext(unitName = "jbugs-persistence")
    private EntityManager entityManager;

    /**
     * Map's Notification to the DataBase's Notification Table and add a Notification
     *
     * @param notification to be added
     * @return the added Notification
     */
    public Notification addNotification(Notification notification) {
        entityManager.persist(notification);
        entityManager.flush();
        return notification;
    }

    /**
     * Finds a Notification by Notification's id
     *
     * @param id of the Notification
     * @return Notification with the given id
     */
    public Notification findNotification(Integer id) {
        return entityManager.find(Notification.class, id);
    }

    /**
     * Deleted to Notifications belonging to a certain User
     *
     * @param user whom notification to be deleted
     * @return number of deleted Notifications
     */
    public Integer deleteNotificationAfterUserId(User user) {
        Query query = entityManager.createNamedQuery(Notification.DELETE_NOTIFICATION_AFTER_USER_ID);
        query.setParameter("user", user);
        return query.executeUpdate();
    }

    /**
     * Finds all Notification belonging to a User by the User's username
     *
     * @param username of the User which Notifications to be searched
     * @return List of Notifications of the User with the given username
     */
    public List<Notification> findAllNotificationsByUsername(String username) {
        Query query = entityManager.createNamedQuery(Notification.FIND_ALL_NOTIFICATION_FOR_AN_USER_BY_USERNAME);
        query.setParameter("username", username);
        return query.getResultList();
    }

    /**
     * Updated a notification(manly user to update the isSeen field of the notification)
     *
     * @param notification to be updated
     * @return the updated Notification
     */
    public Notification update(Notification notification) {
        return entityManager.merge(notification);
    }

    /**
     * Removes old notifications
     *
     * @return the number of comments deleted
     */
    public Integer removeOld() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -360);
        java.sql.Date oneYear = new java.sql.Date(cal.getTimeInMillis());
        Query query = entityManager.createNamedQuery(Notification.REMOVE_OLD_NOTIFICATIONS);
        query.setParameter("expiryDate", oneYear);
        return query.executeUpdate();
    }

    /**
     * Removes notifications older than a given time interval
     *
     * @param miliExpirationDelta time interval for which the notifications are kept
     * @return the number of notifications deleted
     */
    public Integer removeOld(Integer miliExpirationDelta) {
        java.sql.Timestamp oneYear = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis() - miliExpirationDelta);
        Query query = entityManager.createNamedQuery(Notification.REMOVE_OLD_NOTIFICATIONS);
        query.setParameter("expiryDate", oneYear);
        return query.executeUpdate();
    }

}
