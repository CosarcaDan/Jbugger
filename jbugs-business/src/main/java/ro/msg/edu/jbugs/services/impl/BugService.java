package ro.msg.edu.jbugs.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfWriter;
import ro.msg.edu.jbugs.dto.AttachmentDto;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.dto.mappers.AttachmentDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.BugDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.UserDtoMapping;
import ro.msg.edu.jbugs.entity.Attachment;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.exceptions.RepositoryException;
import ro.msg.edu.jbugs.repo.BugRepo;
import ro.msg.edu.jbugs.repo.UserRepo;
import ro.msg.edu.jbugs.validators.Validator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The service for the Bug Repository created
 * in the Persistence Layer.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */

@Stateless
public class BugService {

    private String filesPrefix = "files/";

    //injects the Bug Repository
    @EJB
    private BugRepo bugRepo;

    //injects the User Repository
    @EJB
    private UserRepo userRepo;

    //injects the service of the notifications
    @EJB
    private NotificationService notificationService;

    /**
     * Adds a new bug in the database after validating it
     * and finding the user that created the bug and the user
     * to whom the bug was assigned by their Id.
     *
     * @param bugDto - BugDto, the bugDto that comes from the
     *               client and has to be inserted
     * @return the bugDto that was inserted in the database
     */
    public BugDto addBug(BugDto bugDto) throws BusinessException {
        Validator.validateBug(bugDto);
        User creator = userRepo.findUser(Integer.parseInt(bugDto.getCreated()));
        User assigned = userRepo.findUser(Integer.parseInt(bugDto.getAssigned()));
        Bug bug = BugDtoMapping.bugDtoToBug(bugDto, creator, assigned);
        return BugDtoMapping.bugToBugDtoComplete(bugRepo.addBug(bug));
    }

    /**
     * Finds a bug after the given id that comes from the client.
     * @param id - Integer; the id for the search criteria
     * @return bugDto, if the bug was found
     * @exception BusinessException is no bug with the give id was found
     * */
    public BugDto findBug(Integer id) throws BusinessException {
        try {
            Bug bug = bugRepo.findBug(id);
            BugDto bugDto = BugDtoMapping.bugToBugDtoComplete(bug);
            return bugDto;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("Bug with given id not found", "msg - 014");
        }
    }

    /**
     * @return a list of all bugDtos that has to be sent
     * to the client
     */
    public List<BugDto> getAllBugs() {
        List<Bug> bugs = bugRepo.getAllBugs();
        List<BugDto> bugDtos = bugs
                .stream()
                .map(BugDtoMapping::bugToBugDtoComplete)
                .collect(Collectors.toList());
        return bugDtos;
    }

    /**
     * Sets the criterias for the search of the bugs and returns
     * the bugs that meet the conditions.
     *
     * @param bugSearchCriteria - the bug that contains the criteria
     *                          for the search
     * @return a list of all bugDtos with a certain criteria
     */
    public List<BugDto> getBugsAfterCriteria(BugDto bugSearchCriteria) {
        String creator;
        String assigned;
        List<Bug> bugList;
        Bug.Status status;
        Bug.Severity severity;
        try {
            //check if any creator was selected
            creator = userRepo.findUserByUsername(bugSearchCriteria.getCreated()).getUsername();
        } catch (RepositoryException e) {
            //if no creator was selected, then select all users
            creator = "%";
        }
        try {
            //check if any user was assigned
            assigned = userRepo.findUserByUsername(bugSearchCriteria.getAssigned()).getUsername();
        } catch (RepositoryException e) {
            //if no assigned user was selected, then select all users
            assigned = "%";
        }
        //checks if severities and statuses were selected
        if (bugSearchCriteria.getSeverity().equals("") || bugSearchCriteria.getStatus().equals("")) {
            if (!bugSearchCriteria.getStatus().equals("")) {
                status = Bug.Status.valueOf(bugSearchCriteria.getStatus());
                bugList = bugRepo.getBugsAfterSearchCriteriaWithStatus(status, creator, assigned);
            } else if (!bugSearchCriteria.getSeverity().equals("")) {
                severity = Bug.Severity.valueOf(bugSearchCriteria.getSeverity());
                bugList = bugRepo.getBugsAfterSearchCriteriaWithSeverity(severity, creator, assigned);
            } else {
                bugList = bugRepo.getBugsAfterSearchCriteriaUsers(creator, assigned);
            }
            //severity and status were selected
        } else {
            severity = Bug.Severity.valueOf(bugSearchCriteria.getSeverity());
            status = Bug.Status.valueOf(bugSearchCriteria.getStatus());
            bugList = bugRepo.getBugsAfterSearchCriteriaWithSeverityAndStatus(severity, status, creator, assigned);
        }
        return bugList.stream().map(BugDtoMapping::bugToBugDtoComplete).collect(Collectors.toList());
    }

    /**
     * Deletes all bugs that are older that one year.
     * @return 1 if any bug was deleted
     *         0 if no bug was deleted
     * */
    public Integer deleteOldBugs() {
        Date date = new Date();
        date.setYear(date.getYear() - 1);
        return bugRepo.deleteOldBugs(date);
    }

    /**
     * Updates the status of a bug after the diagramm presented
     * in the JBugger documentation.
     * @param bugDto - BugDto; the bug whose status has to be
     *               changed
     * @return the updated bugDto
     * */
    public BugDto updateStatusBug(BugDto bugDto) throws BusinessException {
        Bug bug = bugRepo.findBug(bugDto.getId());
        Bug.Status newStatus = Bug.Status.valueOf(bugDto.getStatus());
        if (bug.getStatus().equals(Bug.Status.NEW)) {
            if (newStatus.equals(Bug.Status.REJECTED) || newStatus.equals(Bug.Status.IN_PROGRESS)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplete(bug);
            }
        } else if (bug.getStatus().equals(Bug.Status.IN_PROGRESS)) {
            if (newStatus.equals(Bug.Status.REJECTED) || newStatus.equals(Bug.Status.INFONEEDED) ||
                    newStatus.equals(Bug.Status.FIXED)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplete(bug);
            }
        } else if (bug.getStatus().equals(Bug.Status.INFONEEDED)) {
            if (newStatus.equals(Bug.Status.IN_PROGRESS)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplete(bug);
            }
        } else if (bug.getStatus().equals(Bug.Status.FIXED)) {
            if (newStatus.equals(Bug.Status.NEW) || newStatus.equals(Bug.Status.CLOSED)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplete(bug);
            }
        } else if ((bug.getStatus().equals(Bug.Status.REJECTED))) {
            if (newStatus.equals(Bug.Status.CLOSED)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplete(bug);
            }
        }
        throw new BusinessException("Invalid Bug Status progression", "msg - 016");

    }

    /**
     * Closes the status of the given bug if the bug is not closed
     * yet or the actual status is REJECTED or FIXED like in the
     * JBugger documentation.
     * @param bugDto - BugDto; the bug that has to e closed
     * @return the bug with its status
     * */
    public BugDto closeBug(BugDto bugDto) throws BusinessException {
        Bug bug = bugRepo.findBug(bugDto.getId());
        if (bug.getStatus().equals(Bug.Status.CLOSED))
            throw new BusinessException("Bug already closed", "msg - 017");
        if (!bug.getStatus().equals(Bug.Status.FIXED) && !(bug.getStatus().equals(Bug.Status.REJECTED)))
            throw new BusinessException("Bug has to to FIXED or REJECTED in order to be CLOSED", "msg - 018");
        bug.setStatus(Bug.Status.CLOSED);
        try {
            BugDto closedBugDto = BugDtoMapping.bugToBugDtoComplete(bug);
            UserDto creatorDto = UserDtoMapping.
                    userToUserDtoWithoutBugId(userRepo.findUserByUsername(closedBugDto.getCreated()));
            UserDto assignedDto = UserDtoMapping.
                    userToUserDtoWithoutBugId(userRepo.findUserByUsername(closedBugDto.getAssigned()));
            notificationService.createNotificationCloseBug(creatorDto, assignedDto, closedBugDto);
            return closedBugDto;
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Checks if only the status of the bug was changed
     *
     * @param bugDto - the bugDto from the client with the
     *               changed
     * @return true if only the status was changed
     * false if at least one other attribute was changed
     */
    private boolean isOnlyStatusChanged(BugDto bugDto, Bug bug) {
        if (!bugDto.getTitle().equals(bug.getTitle()))
            return false;
        if (!bugDto.getVersion().equals(bug.getVersion()))
            return false;
        if (!bugDto.getFixedVersion().equals(bug.getFixedVersion()))
            return false;
        if (!bugDto.getDescription().equals(bug.getDescription()))
            return false;
        if (!bugDto.getSeverity().equals(bug.getSeverity().toString()))
            return false;
        if (!bugDto.getTargetDate().equals(bug.getTargetDate()))
            return false;
        return bugDto.getAssigned().equals(bug.getAssigned().getUsername());
        //todo changed attachment
    }

    /**
     * Validates the bug and updates it in the table if the bug is
     * found. Before the update, it also checks if the assigned user
     * and the creator of the bug exist in the database.
     * @param bugDto - BugDto; the bug that has to be updated
     * @return bugDto - the updated bug
     * @exception BusinessException - if the bug has been assigned/ created
     * by someone who does not exist in the database
     * */
    public BugDto updateBug(BugDto bugDto) throws BusinessException {
        Validator.validateBug(bugDto);
        Bug bug = bugRepo.findBug(bugDto.getId());
        BugDto oldBugDto = BugDtoMapping.bugToBugDtoComplete(bug);
        boolean onlyStatusChanged = isOnlyStatusChanged(bugDto, bug);
        bug.setTitle(bugDto.getTitle());
        bug.setDescription(bugDto.getDescription());
        bug.setVersion(bugDto.getVersion());
        bug.setTargetDate(bugDto.getTargetDate());
        bug.setFixedVersion(bugDto.getFixedVersion());
        bug.setSeverity(Bug.Severity.valueOf(bugDto.getSeverity()));
        try {
            User assigned = userRepo.findUserByUsername(bugDto.getAssigned());
            UserDto creatorDto = UserDtoMapping.userToUserDtoWithoutBugId(userRepo.findUserByUsername(bugDto.getCreated()));
            UserDto assignedDto = UserDtoMapping.userToUserDtoWithoutBugId(userRepo.findUserByUsername(bugDto.getAssigned()));
            bug.setAssigned(assigned);
            if (!bug.getStatus().equals(Bug.Status.valueOf(bugDto.getStatus())))
                updateStatusBug(bugDto);
            BugDto updatedBug = BugDtoMapping.bugToBugDtoComplete(bug);
            //send notification for edit status of the bug if only the status was changed
            if (onlyStatusChanged) {
                notificationService.createNotificationBugEditOnlyStatus(oldBugDto, updatedBug, creatorDto, assignedDto);
                //send edit bug notification if the values of the bug were changed
            } else {
                notificationService.createNotificationBugEdit(oldBugDto, updatedBug, creatorDto, assignedDto);
            }
            return updatedBug;
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Creates a new PDF file in which all details of a given bug
     * are written.
     * @param bugDto - BugDto; the bug whose details should be
     *               written in the new file
     * @return the URL, where the created PDF file can be downloaded
     * */
    public String makePDF(BugDto bugDto) throws IOException, DocumentException {
        Bug bug = bugRepo.findBug(bugDto.getId());

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(filesPrefix + bug.getTitle() + ".pdf"));
        document.open();
        Font titleFont = FontFactory.getFont("/fonts/Roboto-Bold.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 24, Font.NORMAL, BaseColor.BLACK);
        Chunk titleChunk = new Chunk("Bug #" + bug.getId(), titleFont);

        Paragraph titleParagraph = new Paragraph();
        titleParagraph.add(titleChunk);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);

        Font subtitleFont = FontFactory.getFont("/fonts/Roboto-Regular.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.NORMAL, BaseColor.BLACK);
        Chunk subtitleChunk = new Chunk(bug.getTitle(), subtitleFont);

        Paragraph subtitleParagraph = new Paragraph();
        subtitleParagraph.add(subtitleChunk);
        subtitleParagraph.setAlignment(Element.ALIGN_CENTER);
        subtitleParagraph.setMultipliedLeading(2.0f);
        subtitleParagraph.setSpacingAfter(2.5f);
        document.add(subtitleParagraph);

        List<Paragraph> paragraphList = new ArrayList<>();
        newElement("Description: ", bug.getDescription()).forEach(p -> paragraphList.add(p));
        paragraphList.add(newShortElement("Version: ", bug.getVersion()));
        paragraphList.add(newShortElement("Target date: ", bug.getTargetDate().toString().split(" ")[0]));
        paragraphList.add(newShortElement("Status: ", bug.getStatus().name()));
        paragraphList.add(newShortElement("Fixed version: ", bug.getFixedVersion()));
        paragraphList.add(newShortElement("Severity: ", bug.getSeverity().name()));
        paragraphList.add(newShortElement("Created by: ", bug.getCreated().getUsername()));
        paragraphList.add(newShortElement("Assigned to: ", bug.getAssigned().getUsername()));
        addAttachments(writer, bug).forEach(p -> paragraphList.add(p));

        for (int i = 0; i < paragraphList.size(); ++i) {
            document.add(paragraphList.get(i));
        }
        document.close();
        return "http://localhost:8080/jbugs/services/files/download/" + bug.getTitle() + ".pdf";
    }

    /**
     * Adds paragraphs into the PDF file with a given
     * font, color and text size.
     * @param title - String; the name of the new paragraph (Field name of the bug)
     * @param text - String; the text of the paragraph (Field value of the bug)
     * @return the list of the created paragraphs
     * */
    private List<Paragraph> newElement(String title, String text) {
        ArrayList<Paragraph> result = new ArrayList<>();
        Font subtitleFont = FontFactory.getFont("/fonts/Roboto-Bold.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.NORMAL, BaseColor.BLACK);
        Font font = FontFactory.getFont("/fonts/Roboto-Regular.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.BLACK);
        Chunk subtitleChunk = new Chunk(title, subtitleFont);
        Paragraph subtitleParagraph = new Paragraph();
        subtitleParagraph.add(subtitleChunk);
        subtitleParagraph.setAlignment(Element.ALIGN_LEFT);
        subtitleParagraph.setMultipliedLeading(2.0f);
        result.add(subtitleParagraph);
        Chunk textChunk = new Chunk(text, font);
        Paragraph textParagraph = new Paragraph();
        textParagraph.add(textChunk);
        textParagraph.setAlignment(Element.ALIGN_LEFT);
        textParagraph.setMultipliedLeading(1.5f);
        textParagraph.setSpacingAfter(1.5f);
        result.add(textParagraph);
        return result;
    }

    /**
     * Adds paragraphs into the PDF file with a given
     * font, color and text size.
     * @param title - String; the name of the new paragraph (Field name of the bug)
     * @param text - String; the text of the paragraph (Field value of the bug)
     * @return the list of the created paragraphs
     * */
    private Paragraph newShortElement(String title, String text) {
        Font subtitleFont = FontFactory.getFont("/fonts/Roboto-Bold.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.NORMAL, BaseColor.BLACK);
        Font font = FontFactory.getFont("/fonts/Roboto-Regular.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.NORMAL, BaseColor.BLACK);
        Chunk subtitleChunk = new Chunk(title, subtitleFont);
        Chunk textChunk = new Chunk(text, font);
        Paragraph result = new Paragraph();
        result.add(subtitleChunk);
        result.add(textChunk);
        result.setAlignment(Element.ALIGN_LEFT);
        result.setMultipliedLeading(2.0f);
        return result;
    }


    /**
     * Adds the attachments of the bug in the PDF file of the given bug.
     * @param bug - the bug whose attachments must be written in the file
     * @return the list of the created paragraphs for each attachment
     * */
    private List<Paragraph> addAttachments(PdfWriter writer, Bug bug) {
        ArrayList<Paragraph> result = new ArrayList<>();
        Font subtitleFont = FontFactory.getFont("/fonts/Roboto-Bold.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.NORMAL, BaseColor.BLACK);
        Font font = FontFactory.getFont("/fonts/Roboto-Regular.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.BLACK);
        Chunk subtitleChunk = new Chunk("Attachments:", subtitleFont);
        Paragraph subtitleParagraph = new Paragraph();
        subtitleParagraph.add(subtitleChunk);
        subtitleParagraph.setAlignment(Element.ALIGN_LEFT);
        subtitleParagraph.setMultipliedLeading(2.0f);
        result.add(subtitleParagraph);
        bug.getAttachments().forEach(attachment -> {
            PdfFileSpecification fs;
            PdfAnnotation att = null;
            try {
                fs = PdfFileSpecification.fileEmbedded(
                        writer, filesPrefix + attachment.getAttContent(),
                        attachment.getAttContent(), null);
                att = PdfAnnotation.createFileAttachment(writer, null, attachment.getAttContent(), fs);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Chunk textChunk = new Chunk(attachment.getAttContent(), font);
            Chunk attChunk = new Chunk("  OPEN  ", font);
            attChunk.setAnnotation(att);
            Paragraph textParagraph = new Paragraph();
            textParagraph.add(textChunk);
            textParagraph.add(attChunk);
            textParagraph.setAlignment(Element.ALIGN_LEFT);
            textParagraph.setMultipliedLeading(1.5f);
            textParagraph.setSpacingAfter(1.2f);
            result.add(textParagraph);
        });
        return result;
    }

    /**
     * Returns all attachments of a bug.
     * @param bugDto - BugDto; the bug whose attachments
     *               must be returned
     * @return list of all attachments of the given bug
     * */
    public List<AttachmentDto> getAttachments(BugDto bugDto) {
        List<Attachment> attachments = bugRepo.findBug(bugDto.getId()).getAttachments();
        return attachments.stream().map(AttachmentDtoMapping::attachmentToAttachmentDto).collect(Collectors.toList());
    }

    /**
     * Adds a new attachment for a bug.
     * @param bugDto - BugDto; the bug that must
     *               have a new attachment
     * @param attachmentDto - AttachmentDto; the attachment
     *                      that has to be added to the bug.
     * */
    public void addAttachment(BugDto bugDto, AttachmentDto attachmentDto) {
        Bug bug = bugRepo.findBug(bugDto.getId());
        bug.addAttachment(AttachmentDtoMapping.attachmentDtoToAttachment(attachmentDto));
        bugRepo.update(bug);
    }

    /**
     * Deletes an attchment that belongs to a bugDto.
     * @param bugDto - BugDto; the bugDto for which the
     *               attachment must be deleted
     * @param id- Integer; the Id of the bug that has
     *          to be deleted
     * */
    public void deleteAttachment(BugDto bugDto, Integer id) {
        Bug bug = bugRepo.findBug(bugDto.getId());
        bug.removeAttachmentAfterId(id);
        bugRepo.update(bug);
    }
}
