package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Attachment;
import ro.msg.edu.jbugs.entity.Bug;

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
public class AttachmentRepo {

    @PersistenceContext(unitName = "jbugs-persistence")
    EntityManager entityManager;

    public Attachment addAttachment(Attachment attachment) {
        entityManager.persist(attachment);
        entityManager.flush();
        return attachment;
    }

    public Attachment findAttachment(Integer id) {
        return entityManager.find(Attachment.class, id);
    }

    public Integer deleteAttachmentAfterBugId(Bug bug) {
        Query query = entityManager.createNamedQuery(Attachment.DELETE_ATTACHMENTS_AFTER_BUG_ID);
        query.setParameter("bug", bug);
        Integer result = query.executeUpdate();
        return result;
    }

    public void deleteAttachment(Integer id) {
        Query query = entityManager.createNamedQuery(Attachment.DELETE_ATTACHMENT_AFTER_ID);
        query.setParameter("id",id);
        query.executeUpdate();
    }
}
