package ro.msg.edu.jbugs.services.impl;

import ro.msg.edu.jbugs.repo.BugRepo;
import ro.msg.edu.jbugs.repo.UserRepo;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.mappers.BugDtoMapping;
import ro.msg.edu.jbugs.interceptor.CallDurationInterceptor;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;

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
@Interceptors(CallDurationInterceptor.class)
public class BugService {

    @EJB
    private BugRepo bugRepo;

    @EJB
    private UserRepo userRepo;

    public void addBug(BugDto bugDto){
        User creator = userRepo.findUser(bugDto.getCreated().getId());
        User assigned = userRepo.findUser(bugDto.getAssigned().getId());
        Bug bug = BugDtoMapping.bugDtoToBug(bugDto,creator,assigned);
        bugRepo.addBug(bug);
    }

    public BugDto findBug(Integer id){
        Bug bug = bugRepo.findBug(id);
        BugDto bugDto = BugDtoMapping.bugToBugDtoComplet(bug);
        return bugDto;
    }


    public List<BugDto> getAllBug(){
        List<Bug> bugList = bugRepo.getAllBugs();
        List<BugDto> bugDtoList = bugList.stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
        return bugDtoList;
    }

    public Integer deleteOldBugs(){
        Date date = new Date();
        date.setYear(date.getYear() - 1);
        Integer result = bugRepo.deleteOldBugs(date);
        return result;
    }
}
