package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.dto.AttachmentDto;
import ro.msg.edu.jbugs.dto.mappers.AttachmentDtoMapping;
import ro.msg.edu.jbugs.entity.Attachment;
import ro.msg.edu.jbugs.repo.AttachmentRepo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Stateless
public class AttachmentService {

    @EJB
    private AttachmentRepo attachmentRepo;

    public void addAttachment(AttachmentDto attachmentDto) {
        Attachment attachment = AttachmentDtoMapping.attachmentDtoToAttachment(attachmentDto);
        attachmentRepo.addAttachment(attachment);
    }

//    public AttachmentDto findAttachment(Integer id){
//        Attachment attachment = attachmentRepo.findAttachment(id);
//        AttachmentDto attachmentDto = AttachmentDtoMapping.attachmentToAttachmentDto(attachment);
//        return attachmentDto;
//    }
}
