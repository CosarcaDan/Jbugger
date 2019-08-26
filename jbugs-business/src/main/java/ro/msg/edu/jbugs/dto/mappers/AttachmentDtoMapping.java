package ro.msg.edu.jbugs.dto.mappers;

import ro.msg.edu.jbugs.dto.AttachmentDto;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.entity.Attachment;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class AttachmentDtoMapping {

    public static Attachment attachmentDtoToAttachmentWithBug(AttachmentDto attachmentDto, BugDto bugDto) {
        Attachment attachment = new Attachment();
        attachment.setId(attachmentDto.getId());
        attachment.setAttContent(attachmentDto.getAttContent());
        attachment.setBug(BugDtoMapping.bugDtoToBugIncomplet(bugDto));
        return attachment;
    }

    public static Attachment attachmentDtoToAttachment(AttachmentDto attachmentDto) {
        Attachment attachment = new Attachment();
        attachment.setId(attachmentDto.getId());
        attachment.setAttContent(attachmentDto.getAttContent());
        return attachment;
    }

    public static AttachmentDto attachmentToAttachmentDto(Attachment attachment){
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(attachment.getId());
        attachmentDto.setAttContent(attachment.getAttContent());
        return attachmentDto;
    }
}
