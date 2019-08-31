package ro.msg.edu.jbugs.dto.mappers;

import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;

/**
 * Convert an object of type Bug to an object of type
 * BugDto or vice versa.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
public class BugDtoMapping {

    /**
     * Converts an object of type BugDto to an object of
     * type Bug.
     *
     * @param bugDto   - the BugDto that has to be converted
     * @param creator  - User; the user that created the bug
     * @param assigned - User; the user to whom the bug
     *                 was assigned
     * @return an object of type Bug
     */
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

    /**
     * Converts an object of type Bug to an object of
     * type BugDto.
     * @param bug - the Bug that has to be converted
     * @return an object of type BugDto
     * */
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
