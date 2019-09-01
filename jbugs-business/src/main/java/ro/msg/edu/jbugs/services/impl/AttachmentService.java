package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.dto.AttachmentDto;
import ro.msg.edu.jbugs.dto.mappers.AttachmentDtoMapping;
import ro.msg.edu.jbugs.entity.Attachment;
import ro.msg.edu.jbugs.repo.AttachmentRepo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * The service for the Attachment Repository created
 * in the Persistence Layer.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
@Stateless
public class AttachmentService {

    //injects the Attachment Repository
    @EJB
    private AttachmentRepo attachmentRepo;

    /**
     * Adds a new attachment in the table after
     * converting it from AttachmentDto to Attachment.
     *
     * @param attachmentDto - AttachmentDto; the attachment
     *                      that must be inserted
     */
    public void addAttachment(AttachmentDto attachmentDto) {
        Attachment attachment = AttachmentDtoMapping.attachmentDtoToAttachment(attachmentDto);
        attachmentRepo.addAttachment(attachment);
    }

    /**
     * Deletes an attachment after its id
     *
     * @param id - Integer; the id pf the
     *           attachment that must be removed
     */
    public Integer deleteAttachment(Integer id) {
        return attachmentRepo.deleteAttachment(id);
    }

}
