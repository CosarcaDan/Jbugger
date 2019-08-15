package ro.msg.edu.jbugs.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */

@Entity
@Table(name = "comments")
@NamedQueries({
        @NamedQuery(name = Comment.DELETE_OLD_COMMENT, query = "delete from Comment c where c.date < :date "),
        @NamedQuery(name = Comment.DELETE_COMMENT_AFTER_USER_ID, query = "delete from Comment c where c.user = :user")
})
public class Comment implements Serializable {
    public static final String DELETE_OLD_COMMENT = "deleteOldComment";
    public static final String DELETE_COMMENT_AFTER_USER_ID = "deleteCommentAfterUserID";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "text")
    private String text;

    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "bug_id")
    private Bug bug;

    public Comment() {
    }

    public Comment(String text, Date date, User user, Bug bug) {
        this.text = text;
        this.date = date;
        this.user = user;
        this.bug = bug;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }
}
