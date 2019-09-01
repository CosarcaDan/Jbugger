package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Comment;
import ro.msg.edu.jbugs.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.Date;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Stateless
public class CommentRepo {
    @PersistenceContext(unitName = "jbugs-persistence")
    EntityManager entityManager;

    public Comment addComment(Comment comment) {
        entityManager.persist(comment);
        entityManager.flush();
        return comment;
    }

    public Integer deleteOldComments(Date date) {
        Query query = entityManager.createNamedQuery(Comment.DELETE_OLD_COMMENT);
        query.setParameter("date", date);
        Integer result = query.executeUpdate();
        return result;
    }
    public Integer removeOld()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -360);
        java.sql.Date oneYear = new java.sql.Date(cal.getTimeInMillis());
        Query query = entityManager.createNamedQuery(Comment.REMOVE_OLD_COMMENTS);
        query.setParameter("expiryDate",oneYear);
        return query.executeUpdate();
    }

    public Integer deleteCommentAfterUserId(User user) {
        Query query = entityManager.createNamedQuery(Comment.DELETE_COMMENT_AFTER_USER_ID);
        query.setParameter("user", user);
        Integer result = query.executeUpdate();
        return result;
    }
}
