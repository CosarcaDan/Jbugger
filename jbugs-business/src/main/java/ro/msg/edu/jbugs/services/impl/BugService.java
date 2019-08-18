package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.exceptions.BuisnissException;
import ro.msg.edu.jbugs.repo.BugRepo;
import ro.msg.edu.jbugs.repo.UserRepo;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.mappers.BugDtoMapping;
import ro.msg.edu.jbugs.interceptor.CallDurationInterceptor;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.validators.Validator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
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


    @EJB
    private BugRepo bugRepo;

    @EJB
    private UserRepo userRepo;

    public BugDto addBug(BugDto bugDto) throws BuisnissException {
        Validator.validateBug(bugDto);
        User creator = userRepo.findUser(bugDto.getCreated().getId());
        User assigned = userRepo.findUser(bugDto.getAssigned().getId());
        Bug bug = BugDtoMapping.bugDtoToBug(bugDto, creator, assigned);
        bug.setStatus(Bug.Status.NEW);
        return BugDtoMapping.bugToBugDtoIncomplet(bugRepo.addBug(bug));
    }

    public BugDto findBug(Integer id) {
        Bug bug = bugRepo.findBug(id);
        BugDto bugDto = BugDtoMapping.bugToBugDtoComplet(bug);
        return bugDto;
    }


    public List<BugDto> getAllBug() {
        List<Bug> bugList = bugRepo.getAllBugs();
        List<BugDto> bugDtoList = bugList.stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
        return bugDtoList;
    }

    public Integer deleteOldBugs() {
        Date date = new Date();
        date.setYear(date.getYear() - 1);
        Integer result = bugRepo.deleteOldBugs(date);
        return result;
    }

    public BugDto updateStatusBug(BugDto bugDto) throws BuisnissException {
        Bug bug = bugRepo.findBug(bugDto.getId());
        Bug.Status newStatus = Bug.Status.valueOf(bugDto.getStatus());
        if (bug.getStatus().equals(Bug.Status.NEW)) {
            if (newStatus.equals(Bug.Status.REJECTED) || newStatus.equals(Bug.Status.IN_PROGRESS)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoIncomplet(bug);
            }
        } else if (bug.getStatus().equals(Bug.Status.IN_PROGRESS)) {
            if (newStatus.equals(Bug.Status.REJECTED) || newStatus.equals(Bug.Status.INFONEEDED) || newStatus.equals(Bug.Status.FIXED)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoIncomplet(bug);
            }
        } else if (bug.getStatus().equals(Bug.Status.INFONEEDED)) {
            if (newStatus.equals(Bug.Status.IN_PROGRESS)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoIncomplet(bug);
            }
        } else if (bug.getStatus().equals(Bug.Status.FIXED)) {
            if (newStatus.equals(Bug.Status.NEW) || newStatus.equals(Bug.Status.CLOSED)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoIncomplet(bug);
            }
        } else if ((bug.getStatus().equals(Bug.Status.REJECTED))) {
            if (newStatus.equals(Bug.Status.CLOSED)) {
                bug.setStatus(newStatus);
                return BugDtoMapping.bugToBugDtoIncomplet(bug);
            }
        }
        throw new BuisnissException("Invalid Bug Status progression", "msg - 011");

    }

    public BugDto closeBug(BugDto bugDto) throws BuisnissException {
        Bug bug = bugRepo.findBug(bugDto.getId());
        if (bug.getStatus().equals(Bug.Status.CLOSED))
            throw new BuisnissException("Bug already closed", "msg - 010");
        if (!bug.getStatus().equals(Bug.Status.FIXED) && !(bug.getStatus().equals(Bug.Status.REJECTED)))
            throw new BuisnissException("Bug has to to FIXED or REJECTED in order to be CLOSED", "msg - 011");
        bug.setStatus(Bug.Status.CLOSED);
        return BugDtoMapping.bugToBugDtoIncomplet(bug);
    }

    //Todo Validations
    public BugDto updateBug(BugDto bugDto) throws BuisnissException {
        Validator.validateBug(bugDto);
        Bug bug = bugRepo.findBug(bugDto.getId());
        bug.setTitle(bugDto.getTitle());
        bug.setDescription(bugDto.getDescription());
        bug.setVersion(bugDto.getVersion());
        bug.setTargetDate(bugDto.getTargetDate());
        bug.setFixedVersion(bugDto.getFixedVersion());
        bug.setSeverity(Bug.Severity.valueOf(bugDto.getSeverity()));
        updateStatusBug(bugDto);
        return BugDtoMapping.bugToBugDtoIncomplet(bug); //todo see if status has been schanged
    }

    //ToDo export bug excel

}
