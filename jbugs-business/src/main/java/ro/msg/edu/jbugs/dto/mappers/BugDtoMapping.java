package ro.msg.edu.jbugs.dto.mappers;

import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class BugDtoMapping {

    public static Bug bugDtoToBug(BugDto bugDto, User creator, User assigned) {
        Bug bug = new Bug();
        bug.setId(bugDto.getId());
        bug.setTitle(bugDto.getTitle());
        bug.setDescription(bugDto.getDescription());
        bug.setVersion(bugDto.getVersion());
        bug.setTargetDate(bugDto.getTargetDate());
        bug.setStatus(Bug.Status.valueOf(bugDto.getStatus()));
        bug.setFixedVersion(bugDto.getFixedVersion());
        bug.setSeverity(Bug.Severity.valueOf(bugDto.getSeverity()));
        bug.setCreated(creator);
        bug.setAssigned(assigned);
        return bug;
    }

    public static Bug bugDtoToBugIncomplete(BugDto bugDto) {
        Bug bug = new Bug();
        bug.setId(bugDto.getId());
        bug.setTitle(bugDto.getTitle());
        bug.setDescription(bugDto.getDescription());
        bug.setVersion(bugDto.getVersion());
        bug.setTargetDate(bugDto.getTargetDate());
        bug.setStatus(Bug.Status.valueOf(bugDto.getStatus()));
        bug.setFixedVersion(bugDto.getFixedVersion());
        bug.setSeverity(Bug.Severity.valueOf(bugDto.getSeverity()));
        return bug;
    }

    public static BugDto bugToBugDtoComplete(Bug bug) {
        BugDto bugDto = new BugDto();
        bugDto.setId(bug.getId());
        bugDto.setTitle(bug.getTitle());
        bugDto.setDescription(bug.getDescription());
        bugDto.setVersion(bug.getVersion());
        bugDto.setTargetDate(bug.getTargetDate());
        bugDto.setStatus(bug.getStatus().toString());
        bugDto.setFixedVersion(bug.getFixedVersion());
        bugDto.setSeverity(bug.getSeverity().toString());
        UserDto userCreatorDto = UserDtoMapping.userToUserDtoWithoutBugId(bug.getCreated());
        bugDto.setCreated(userCreatorDto.getUsername());
        UserDto userAssignedDto = UserDtoMapping.userToUserDtoWithoutBugId(bug.getAssigned());
        bugDto.setAssigned(userAssignedDto.getUsername());
        return bugDto;
    }


}
