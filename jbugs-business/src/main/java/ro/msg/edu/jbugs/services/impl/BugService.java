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
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */

@Stateless
public class BugService {

    private String filesprefix = "files/";

    @EJB
    private BugRepo bugRepo;

    @EJB
    private UserRepo userRepo;

    @EJB
    private NotificationService notificationService;

    public BugDto addBug(BugDto bugDto) throws BusinessException {
        Validator.validateBug(bugDto);
        User creator = userRepo.findUser(Integer.parseInt(bugDto.getCreated()));
        User assigned = userRepo.findUser(Integer.parseInt(bugDto.getAssigned()));
        Bug bug = BugDtoMapping.bugDtoToBug(bugDto, creator, assigned);
        return BugDtoMapping.bugToBugDtoComplet(bugRepo.addBug(bug));
    }

    public BugDto findBug(Integer id) throws BusinessException {
        try {
            Bug bug = bugRepo.findBug(id);
            BugDto bugDto = BugDtoMapping.bugToBugDtoComplet(bug);
            return bugDto;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("Bug with given id not found", "msg - 020");
        }

    }

    public List<BugDto> getAllBug() {
        List<Bug> bugList = bugRepo.getAllBugs();
        List<BugDto> bugDtoList = bugList.stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
        return bugDtoList;
    }

    public List<BugDto> getBugsAfterCriteris(BugDto bugSearchCriteria) {
        String creator;
        String assigned;
        List<Bug> bugList;
        Bug.Status status;
        Bug.Severity severity;
        try {
            creator = userRepo.findUserByUsername(bugSearchCriteria.getCreated()).getUsername();
        } catch (RepositoryException e) {
            creator = "%";
        }
        try {
            assigned = userRepo.findUserByUsername(bugSearchCriteria.getAssigned()).getUsername();
        } catch (RepositoryException e) {
            assigned = "%";
        }
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
        } else {
            severity = Bug.Severity.valueOf(bugSearchCriteria.getSeverity());
            status = Bug.Status.valueOf(bugSearchCriteria.getStatus());
            bugList = bugRepo.getBugsAfterSearchCriteriaWithSeverityAndStatus(severity, status, creator, assigned);
        }

        return bugList.stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
    }

    public Integer deleteOldBugs() {
        Date date = new Date();
        date.setYear(date.getYear() - 1);
        Integer result = bugRepo.deleteOldBugs(date);
        return result;
    }

    public BugDto updateStatusBug(BugDto bugDto) throws BusinessException {
        //todo notification BUG_STATUS_UPDATED: creator assigned
        Bug bug = bugRepo.findBug(bugDto.getId());
        Bug.Status newStatus = Bug.Status.valueOf(bugDto.getStatus());
        if (bug.getStatus().equals(Bug.Status.NEW)) {
            if (newStatus.equals(Bug.Status.REJECTED) || newStatus.equals(Bug.Status.IN_PROGRESS)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplet(bug);
            }
        } else if (bug.getStatus().equals(Bug.Status.IN_PROGRESS)) {
            if (newStatus.equals(Bug.Status.REJECTED) || newStatus.equals(Bug.Status.INFONEEDED) || newStatus.equals(Bug.Status.FIXED)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplet(bug);
            }
        } else if (bug.getStatus().equals(Bug.Status.INFONEEDED)) {
            if (newStatus.equals(Bug.Status.IN_PROGRESS)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplet(bug);
            }
        } else if (bug.getStatus().equals(Bug.Status.FIXED)) {
            if (newStatus.equals(Bug.Status.NEW) || newStatus.equals(Bug.Status.CLOSED)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplet(bug);
            }
        } else if ((bug.getStatus().equals(Bug.Status.REJECTED))) {
            if (newStatus.equals(Bug.Status.CLOSED)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoComplet(bug);
            }
        }
        throw new BusinessException("Invalid Bug Status progression", "msg - 011");

    }

    public BugDto closeBug(BugDto bugDto) throws BusinessException {
        Bug bug = bugRepo.findBug(bugDto.getId());
        if (bug.getStatus().equals(Bug.Status.CLOSED))
            throw new BusinessException("Bug already closed", "msg - 010");
        if (!bug.getStatus().equals(Bug.Status.FIXED) && !(bug.getStatus().equals(Bug.Status.REJECTED)))
            throw new BusinessException("Bug has to to FIXED or REJECTED in order to be CLOSED", "msg - 011");
        bug.setStatus(Bug.Status.CLOSED);
        try {
            BugDto closedBugDto = BugDtoMapping.bugToBugDtoComplet(bug);
            UserDto creatorDto = UserDtoMapping.userToUserDtoWithoutBugId( userRepo.findUserByUsername(closedBugDto.getCreated()));
            UserDto assignedDto = UserDtoMapping.userToUserDtoWithoutBugId(userRepo.findUserByUsername(closedBugDto.getAssigned()));
            notificationService.createNotificationCloseBug(creatorDto,assignedDto,closedBugDto);
            //toDo notification BUG_CLOSED r:creator assigned
            return closedBugDto;
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }
    }

    private boolean isOnlyStatusChanged(BugDto bugdto, Bug bug){
        if(!bugdto.getTitle().equals(bug.getTitle()))
            return false;
        if(!bugdto.getVersion().equals(bug.getVersion()))
            return false;
        if(!bugdto.getFixedVersion().equals(bug.getFixedVersion()))
            return false;
        if(!bugdto.getDescription().equals(bug.getDescription()))
            return false;
        if(!bugdto.getSeverity().equals(bug.getSeverity().toString()))
            return false;
        if(!bugdto.getTargetDate().equals(bug.getTargetDate()))
            return false;
        if(!bugdto.getAssigned().equals(bug.getAssigned().getUsername()))
            return false;
        //todo changed attachemnt
        return true;
    }

    //Todo Validations
    public BugDto updateBug(BugDto bugDto) throws BusinessException {
        Validator.validateBug(bugDto);
        Bug bug = bugRepo.findBug(bugDto.getId());
        BugDto oldBugDto = BugDtoMapping.bugToBugDtoComplet(bug);
        boolean onlyStatusChanged = isOnlyStatusChanged(bugDto,bug);
        bug.setTitle(bugDto.getTitle());
        bug.setDescription(bugDto.getDescription());
        bug.setVersion(bugDto.getVersion());
        bug.setTargetDate(bugDto.getTargetDate());
        bug.setFixedVersion(bugDto.getFixedVersion());
        bug.setSeverity(Bug.Severity.valueOf(bugDto.getSeverity()));
        try {
            User assigned = userRepo.findUserByUsername(bugDto.getAssigned());
            UserDto creatorDto = UserDtoMapping.userToUserDtoWithoutBugId( userRepo.findUserByUsername(bugDto.getCreated()));
            UserDto assignedDto = UserDtoMapping.userToUserDtoWithoutBugId(userRepo.findUserByUsername(bugDto.getAssigned()));
            bug.setAssigned(assigned);
            if (!bug.getStatus().equals(Bug.Status.valueOf(bugDto.getStatus())))
                updateStatusBug(bugDto);
            //todo notification BUG_UPDATED r: bugCreator, AssignedTo
            BugDto updatedBug = BugDtoMapping.bugToBugDtoComplet(bug);
            if(onlyStatusChanged){
                notificationService.createNotificationBugEditOnlyStatus(oldBugDto,updatedBug,creatorDto,assignedDto);
            }else{
                notificationService.createNotificationBugEdit(oldBugDto,updatedBug,creatorDto,assignedDto);
            }
            return updatedBug; //todo see if status has been schanged
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }
    }

    //ToDo export bug excel

    public String makePDF(BugDto bugDto) throws IOException, DocumentException {
        Bug bug = bugRepo.findBug(bugDto.getId());

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filesprefix + bug.getTitle() + ".pdf"));

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
//        manipulatePdf("iTextHelloWorld.pdf","ManiTextHelloWorld.pdf",(String[]) bug.getAttachments().stream().map(Attachment::getAttContent).toArray(String[]::new));

    }

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
//        subtitleParagraph.setSpacingAfter(1.2f);

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

            PdfFileSpecification fs = null;
            PdfAnnotation att = null;
            try {
                fs = PdfFileSpecification.fileEmbedded(
                        writer, filesprefix + attachment.getAttContent(), attachment.getAttContent(), null);
//                writer.addFileAttachment(String.format("Content: %s", attachment.getAttContent()), fs);
                att =
                        PdfAnnotation.createFileAttachment(writer, null, attachment.getAttContent(), fs);
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

    public List<AttachmentDto> getAttachments(BugDto bugDto) {
        List<Attachment> attachments = bugRepo.findBug(bugDto.getId()).getAttachments();
        return attachments.stream().map(AttachmentDtoMapping::attachmentToAttachmentDto).collect(Collectors.toList());
    }

    public void addAttachment(BugDto bugDto, AttachmentDto attachmentDto) {
        Bug bug = bugRepo.findBug(bugDto.getId());
        bug.addAttachment(AttachmentDtoMapping.attachmentDtoToAttachment(attachmentDto));
        bugRepo.update(bug);
    }
}
