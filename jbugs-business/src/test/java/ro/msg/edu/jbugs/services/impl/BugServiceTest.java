package ro.msg.edu.jbugs.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.mappers.BugDtoMapping;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.repo.BugRepo;
import ro.msg.edu.jbugs.repo.UserRepo;

import java.sql.Timestamp;
import java.util.Date;

import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the BugService with
 * Junit by mocking the Bug Repository.
 *
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
@RunWith(MockitoJUnitRunner.class)
public class BugServiceTest {

    private static final String DESCRIPTION = "Lorem Ipsum is simply dummy text of the printing " +
            "and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever " +
            "since the 1500s, when an unknown printer took a galley of type and scrambled it to make a " +
            "type specimen book. It has survived not only five centuries, but also the leap into electronic " +
            "typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release " +
            "of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing " +
            "software like Aldus PageMaker including versions of Lorem Ipsum";

    @InjectMocks
    private BugService bugService;

    @Mock
    private BugRepo bugRepo;

    @Mock
    private UserRepo userRepo;

    public BugServiceTest() {
        this.bugService = new BugService();
    }

    @Test
    public void addBug() throws BusinessException {
//        Date date = new Date();
//        User userCreator = new User(2, "Fnt", "Lnt",
//                "et@msggroup.com", "+40712345678", "pt", "lntf", true);
//        User userAssigned = new User(0,  "Test", "Test",
//                "test@msggroup.com", "+40712345678", "pt", "testt", true);
//        BugDto tempBugDto = new BugDto(1,"Default title", DESCRIPTION,"1.2.3",
//                new Timestamp(date.getTime()), "NEW",
//                "1.2.4","CRITICAL", "1", "2" );
//        userCreator.setId(1);
//        userAssigned.setId(2);
//        when(bugRepo.addBug(BugDtoMapping.bugDtoToBug(tempBugDto, userCreator, userAssigned))).
//                thenReturn(BugDtoMapping.bugDtoToBug(tempBugDto, userCreator, userAssigned));
//        when(userRepo.findUser(userCreator.getId())).thenReturn(userCreator);
//        when(userRepo.findUser(userAssigned.getId())).thenReturn(userAssigned);
//        bugService.addBug(tempBugDto);
    }

    @Test
    public void findBug() throws BusinessException {
        Date date = new Date();
        BugDto tempBugDto = new BugDto(1, "Default title", DESCRIPTION, "1.2.3",
                new Timestamp(date.getTime()), "NEW",
                "1.2.4", "CRITICAL", "1", "2");
        User userCreator = new User(2, "Fnt", "Lnt",
                "et@msggroup.com", "+40712345678", "pt", "lntf", true);
        User userAssigned = new User(0, "Test", "Test",
                "test@msggroup.com", "+40712345678", "pt", "testt", true);
        when(bugRepo.findBug(1)).thenReturn(BugDtoMapping.bugDtoToBug(tempBugDto, userCreator, userAssigned));
        bugService.findBug(1);
    }

    @Test
    public void getAllBugs() {
//        Bug bug1 = new Bug();
//        bug1.setStatus(Bug.Status.NEW);
//        bug1.setSeverity(Bug.Severity.CRITICAL);
//        Bug bug2 = new Bug();
//        bug2.setStatus(Bug.Status.CLOSED);
//        bug2.setSeverity(Bug.Severity.LOW);
//        Bug bug3 = new Bug();
//        bug3.setStatus(Bug.Status.FIXED);
//        bug3.setSeverity(Bug.Severity.MEDIUM);
//        List<Bug> tempBugList = Arrays.asList(bug1, bug2, bug3);
//        when(bugRepo.getAllBugs()).thenReturn(tempBugList);
//        assertEquals(3,bugService.getAllBugs().size());
    }

    @Test
    public void getBugsAfterCriteria() {
    }

    @Test
    public void deleteOldBugs() {
    }

    @Test
    public void updateStatusBug() {
    }

    @Test
    public void closeBug() {
    }

    @Test
    public void updateBug() {
    }

    @Test
    public void makePDF() {
    }

    @Test
    public void getAttachments() {
    }

    @Test
    public void addAttachment() {
    }

    @Test
    public void deleteAttachment() {
    }
}
