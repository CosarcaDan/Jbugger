package ro.msg.edu.jbugs.dto.mappers;

import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
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
        user.setCounter(userDto.getCounter());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        user.setStatus(userDto.getStatus());
        return user;
    }

    public static UserDto userToUserDtoComplet(User user){
        List<BugDto> bugsCreatedDto = new ArrayList<>();
        List<BugDto> bugsAssignedDto = new ArrayList<>();

        if (user.getCreatedBy() != null) {
            bugsCreatedDto = user.getCreatedBy().stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
        }
        if (user.getAssignedTo() != null) {
            bugsAssignedDto = user.getAssignedTo().stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
        }
        return new UserDto(user.getId(),user.getCounter(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getMobileNumber(),user.getPassword(),user.getUsername(),user.getStatus(),bugsCreatedDto,bugsAssignedDto);
    }

    public static UserDto userToUserDtoIncomplet(User user){
        return new UserDto(user.getId(),user.getCounter(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getMobileNumber(),user.getPassword(),user.getUsername(),user.getStatus());

    }

}
