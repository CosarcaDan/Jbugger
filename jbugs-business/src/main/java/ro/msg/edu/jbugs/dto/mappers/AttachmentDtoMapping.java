package ro.msg.edu.jbugs.dto.mappers;

import ro.msg.edu.jbugs.dto.AttachmentDto;
import ro.msg.edu.jbugs.entity.Attachment;

/**
 * Convert an object of type Attachment to an object of type
 * AttachmentDto or vice versa.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
public class AttachmentDtoMapping {

    /**
     * Converts an object of type AttachmentDto to an object of
     * type Attachment.
     *
     * @param attachmentDto - the AttachmentDto that has to be converted
     * @return an object of type Attachment
     */
    public static Attachment attachmentDtoToAttachment(AttachmentDto attachmentDto) {
        Attachment attachment = new Attachment();
        attachment.setId(attachmentDto.getId());
        attachment.setAttContent(attachmentDto.getAttContent());
        return attachment;
    }

    /**
     * Converts an object of type Attachment to an object of
     * type AttachmentDto.
     * @param attachment - the Attachment that has to be converted
     * @return an object of type AttachmentDto
     * */
    public static AttachmentDto attachmentToAttachmentDto(Attachment attachment) {
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(attachment.getId());
        attachmentDto.setAttContent(attachment.getAttContent());
        return attachmentDto;
    }
}
