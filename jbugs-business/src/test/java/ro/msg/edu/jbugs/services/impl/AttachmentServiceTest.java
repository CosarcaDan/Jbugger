package ro.msg.edu.jbugs.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.msg.edu.jbugs.dto.AttachmentDto;
import ro.msg.edu.jbugs.dto.mappers.AttachmentDtoMapping;
import ro.msg.edu.jbugs.repo.AttachmentRepo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the AttachmentService with
 * Junit by mocking the Attachment Repository.
 *
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
@RunWith(MockitoJUnitRunner.class)
public class AttachmentServiceTest {

    @InjectMocks
    private AttachmentService attachmentService;

    @Mock
    private AttachmentRepo attachmentRepo;

    public AttachmentServiceTest() {
        this.attachmentService = new AttachmentService();
    }

    /**
     * Tests the insertion of a new attachment in the
     * database.
     */
    @Test
    public void addAttachment() {
        AttachmentDto attachmentAdded = new AttachmentDto(1, "fileName");
        when(attachmentRepo.addAttachment(AttachmentDtoMapping.attachmentDtoToAttachment(attachmentAdded)))
                .thenReturn(AttachmentDtoMapping.attachmentDtoToAttachment(attachmentAdded));
        attachmentService.addAttachment(attachmentAdded);
    }

    /**
     * Tests the deletion of an attachment from the
     * database.
     */
    @Test
    public void deleteAttachment() {
        when(attachmentRepo.deleteAttachment(2)).thenReturn(1);
        when(attachmentRepo.deleteAttachment(3)).thenReturn(0);
        AttachmentDto attachmentDeleted = new AttachmentDto(2, "attachmentName");
        AttachmentDto attachmentNotDeleted = new AttachmentDto(3, "attachmentName");
        assertEquals((Integer) 1, attachmentService.deleteAttachment(attachmentDeleted.getId()));
        assertEquals((Integer) 0, attachmentService.deleteAttachment(attachmentNotDeleted.getId()));
    }
}
