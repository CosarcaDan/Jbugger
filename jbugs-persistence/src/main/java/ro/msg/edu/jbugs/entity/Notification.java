package ro.msg.edu.jbugs.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Entity
@Table(name = "notifications")
@NamedQueries({
        @NamedQuery(name = Notification.DELETE_NOTIFICATION_AFTER_USER_ID, query = "delete from Notification n where n.user = :user "),
        @NamedQuery(name = Notification.FIND_ALL_NOTIFICATION_FOR_AN_USER_BY_USERNAME, query = "Select n from Notification n where n.user.username = :username")
}
)
public class Notification implements Serializable {
    public static final String DELETE_NOTIFICATION_AFTER_USER_ID = "deleteNotificationAfterUserId";
    public static final String FIND_ALL_NOTIFICATION_FOR_AN_USER_BY_USERNAME = "findAllNotificationForAnUserByUsername";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    private NotificationType type;

    @Column(name = "url")
    private String url;

    @Column(name = "is_seen")
    private boolean isSeen;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    public Notification() {
    }

    public Notification(Timestamp date, String message, NotificationType type, String url, User user) {
        this.date = date;
        this.message = message;
        this.type = type;
        this.url = url;
        this.user = user;
    }

    public Notification(Timestamp date, String message, NotificationType type, String url, boolean isSeen, User user) {
        this.date = date;
        this.message = message;
        this.type = type;
        this.url = url;
        this.isSeen = isSeen;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public enum NotificationType{
        WELCOME_NEW_USER, USER_UPDATED, USER_DELETED, BUG_UPDATED, BUG_CLOSED, BUG_STATUS_UPDATED, USER_DEACTIVATED
    }
}
