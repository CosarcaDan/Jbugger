package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.mappers.BugDtoMapping;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.repo.BugRepo;
import ro.msg.edu.jbugs.repo.UserRepo;
import ro.msg.edu.jbugs.validators.Validator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
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

    public BugDto addBug(BugDto bugDto) throws BusinessException {
        Validator.validateBug(bugDto);
        User creator = userRepo.findeUserAfterUsername(bugDto.getCreated());
        User assigned = userRepo.findeUserAfterUsername(bugDto.getAssigned());
        bugDto.setStatus("NEW");
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
            creator = userRepo.findeUserAfterUsername(bugSearchCriteria.getCreated()).getUsername();
        } catch (BusinessException e) {
            creator = "%";
        }
        try {
            assigned = userRepo.findeUserAfterUsername(bugSearchCriteria.getAssigned()).getUsername();
        } catch (BusinessException e) {
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
        return BugDtoMapping.bugToBugDtoComplet(bug);
    }

    //Todo Validations
    public BugDto updateBug(BugDto bugDto) throws BusinessException {
        Validator.validateBug(bugDto);
        Bug bug = bugRepo.findBug(bugDto.getId());
        bug.setTitle(bugDto.getTitle());
        bug.setDescription(bugDto.getDescription());
        bug.setVersion(bugDto.getVersion());
        bug.setTargetDate(bugDto.getTargetDate());
        bug.setFixedVersion(bugDto.getFixedVersion());
        bug.setSeverity(Bug.Severity.valueOf(bugDto.getSeverity()));
        updateStatusBug(bugDto);
        return BugDtoMapping.bugToBugDtoComplet(bug); //todo see if status has been schanged
    }

    //ToDo export bug excel

}
