package ro.msg.edu.jbugs.services.impl;

import com.google.common.hash.Hashing;
import ro.msg.edu.jbugs.dto.*;
import ro.msg.edu.jbugs.dto.mappers.BugDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.UserDtoMapping;
import ro.msg.edu.jbugs.exceptions.BuisnissException;
import ro.msg.edu.jbugs.repo.UserRepo;
import ro.msg.edu.jbugs.interceptor.CallDurationInterceptor;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
public class UserService {

    @EJB
    private UserRepo userRepo;

    @EJB
    private NotificationService notificationService;

    public User addUser(UserDto userDto) throws IOException {
        userDto.setUsername(generateUserName(userDto.getFirstName(), userDto.getLastName()));
        userDto.setPassword(Hashing.sha256().hashString(userDto.getPassword(), StandardCharsets.UTF_8).toString());
        User user = UserDtoMapping.userDtoToUser(userDto);
        User newUser = userRepo.addUser(user);
        //todo userDto without password
        UserDto newUserDto = UserDtoMapping.userToUserDtoIncomplet(newUser);
        //todo notification type ENUM
        NotificationDto notificationDto = new NotificationDto(0, new Date(), "Welcome new User", "WelcomeNewUser", "noUrl", newUserDto);
        notificationService.addNotification(notificationDto);
        return newUser;
    }

    public UserDto findUser(Integer id) {
        User user = userRepo.findUser(id);
        UserDto userDto = UserDtoMapping.userToUserDtoComplet(user);
        return userDto;
    }

    public List<UserDto> getAllUser() {
        List<User> userList = userRepo.getAllUser();
        List<UserDto> userDtoList = userList.stream().map(UserDtoMapping::userToUserDtoIncomplet).collect(Collectors.toList());
        return userDtoList;
    }

    public List<BugDto> getAllCreatedBugs(Integer id) {
        User user = userRepo.findUser(id);
        List<Bug> bugList = userRepo.getAllCreatedBugs(user);
        List<BugDto> bugDtoList = bugList.stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
        return bugDtoList;
    }

    public List<BugDto> getAllAssignedBugs(Integer id) {
        User user = userRepo.findUser(id);
        List<Bug> bugList = userRepo.getAllAssignedBugs(user);
        List<BugDto> bugDtoList = bugList.stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
        return bugDtoList;
    }

    public String generateUserName(String lastname, String firstname) {
        String firstPart;
        if (lastname.length() >= 5) {
            firstPart = lastname.substring(0, 5);
        } else {
            firstPart = lastname;
        }
        String firstPartLower = firstPart.toLowerCase();
        int charPosition = 0;
        String username = firstPartLower;
        do {
            if (charPosition >= firstname.length()) {
                username = username + "x";
            } else {
                username = (username + firstname.charAt(charPosition)).toLowerCase();
                charPosition++;
            }
        } while (!userRepo.isUsernameUnique(username));

        return username;

    }

    //todo return userDto and throw exception
    public UserDto login(UserDto userDto) throws BuisnissException {
        String encriptedPassword = Hashing.sha256().hashString(userDto.getPassword(), StandardCharsets.UTF_8).toString();
        User user;
        try {
            user = userRepo.login(userDto.getUsername(), encriptedPassword);
            userRepo.resetLoginFailCounter(user);
        }catch (BuisnissException ex){
            user = userRepo.passwordFailed(userDto.getUsername());
        }
        return UserDtoMapping.userToUserDtoIncomplet(user);

    }

    public Integer deleteUser(UserDto userDto) throws BuisnissException {
        return userRepo.deleteUserAfterUserName(userDto.getUsername());
    }

    /////////////

    public void defaultlogin() {

    }

    public void defaultAdd() throws IOException {
        UserDto userDto1 = new UserDto(0, 0, "toBeDeleted", "toBeDeleted", "toBeDeleted", "toBeDeleted", "toBeDeleted", "toBeDeleted", false);
        //UserDto userDto2 = new UserDto(0, 0, "toBeDeleted", "toBeDeleted", "toBeDeleted", "toBeDeleted", "toBeDeleted", "toBeDeleted2", false);
        addUser(userDto1);
        //addUser(userDto2);

    }

    public void defaultFindAll() throws Exception {
        if (!userRepo.getAllDToBeDeleted())
            throw new Exception("default find all user failed");
    }

    public void defaultDelete() throws Exception {
        if (!userRepo.deleteDefault())
            throw new Exception("default find all user failed");
    }

    public void defaultTest() throws Exception {
        defaultAdd();
        defaultFindAll();
        defaultDelete();
    }

}
