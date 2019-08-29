package ro.msg.edu.jbugs.dto.mappers;

import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class UserDtoMapping {
    private UserDtoMapping() {
    }

    public static User userDtoToUser(UserDto userDto){
        //todo poate gasit alternativa pentru mappind fara sa facem new User, alternativ trimis atribute la add si construim acolo Userul
        User user =  new User();
        user.setId(userDto.getId());
        user.setFailedLoginAttempt(userDto.getFailedLoginAttempt());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        user.setStatus(userDto.getStatus());
        return user;
    }

    //Not in use
    public static UserDto userToUserDtoWithBugsId(User user) {
        List<Integer> bugsCreatedDto = new ArrayList<>();
        List<Integer> bugsAssignedDto = new ArrayList<>();

        if (user.getCreatedBugs() != null) {
            bugsCreatedDto = user.getCreatedBugs().stream().map(Bug::getId).collect(Collectors.toList());
        }
        if (user.getAssignedBugs() != null) {
            bugsAssignedDto = user.getAssignedBugs().stream().map(Bug::getId).collect(Collectors.toList());
        }
        return new UserDto(user.getId(), user.getFailedLoginAttempt(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getMobileNumber(), user.getPassword(), user.getUsername(), user.getStatus(), bugsCreatedDto, bugsAssignedDto);
    }

    public static UserDto userToUserDtoWithoutBugId(User user) {
        return new UserDto(user.getId(), user.getFailedLoginAttempt(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getMobileNumber(), user.getPassword(), user.getUsername(), user.getStatus());

    }

}
