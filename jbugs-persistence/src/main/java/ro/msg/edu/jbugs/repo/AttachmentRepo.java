package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Attachment;
import ro.msg.edu.jbugs.entity.Bug;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Repository for the attachment entity. The entity
 * manager accesses the database and adds or removes
 * rows of the attachments table.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */

@Stateless
public class AttachmentRepo {

    //accesses the attachments table
    @PersistenceContext(unitName = "jbugs-persistence")
    EntityManager entityManager;

    /**
     * Adds a new attachment in the attachments table.
     *
     * @param attachment - Atachment; the attachmnt that has to be
     *                   inserted into the table
     * @return the attachment that was inserted into the table
     */
    public Attachment addAttachment(Attachment attachment) {
        entityManager.persist(attachment);
        entityManager.flush();
        return attachment;
    }

    /**
     * Deletes an attachment after a given id of the bug.
     * @param bug - Bug; the bug that has the attachment
     * @return 1 if any attachment was deleted
     *         0 if no attachment was deleted
     * */
    public Integer deleteAttachmentAfterBugId(Bug bug) {
        Query query = entityManager.createNamedQuery(Attachment.DELETE_ATTACHMENTS_AFTER_BUG_ID);
        query.setParameter("bug", bug);
        Integer result = query.executeUpdate();
        return result;
    }

    /**
     * Deletes an attachment after its Id.
     * @param id - Integer; the id of the attachment that
     *           has to be deleted
     * */
    public void deleteAttachment(Integer id) {
        Query query = entityManager.createNamedQuery(Attachment.DELETE_ATTACHMENT_AFTER_ID);
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
