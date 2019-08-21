package ro.msg.edu.jbugs.services.impl;

import com.google.common.hash.Hashing;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.dto.mappers.BugDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.UserDtoMapping;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.entity.Role;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.repo.RoleRepo;
import ro.msg.edu.jbugs.repo.UserRepo;
import ro.msg.edu.jbugs.validators.Validator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Stateless
public class UserService {

    //Todo ? sa schimbam tot inputul si outputul in UserDto sau permitem si primitive ?
    //ToDo create notification for specified methods
    //ToDo validate pentru id

    @EJB
    private UserRepo userRepo;

    @EJB
    private RoleRepo roleRepo;

    @EJB
    private NotificationService notificationService;

    //ToDo validate Data
    public User addUser(UserDto userDto) throws BusinessException {
        Validator.validateUser(userDto);
        userDto.setUsername(generateUserName(userDto.getFirstName(), userDto.getLastName()));
        String defaultPassword = "defaultPass";
        userDto.setPassword(Hashing.sha256().hashString(defaultPassword, StandardCharsets.UTF_8).toString());
        userDto.setCounter(0);
        userDto.setStatus(true);
        User user = UserDtoMapping.userDtoToUser(userDto);
        User newUser = userRepo.addUser(user);
        //todo userDto without password
        //UserDto newUserDto = UserDtoMapping.userToUserDtoIncomplet(newUser);
        //todo notification type ENUM
        //NotificationDto notificationDto = new NotificationDto(0, new Date(), "Welcome new User", "WelcomeNewUser", "noUrl", newUserDto);
        //notificationService.addNotification(notificationDto);
        return newUser;
    }

    public UserDto findUser(Integer id) throws BusinessException {
        try {
            User user = userRepo.findUser(id);
            UserDto userDto = UserDtoMapping.userToUserDtoComplet(user);
            return userDto;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("No User found with given Id", "msg - 006");
        }
    }
    public UserDto findUser(String username) throws BusinessException {
        try {
            User user = userRepo.findeUserAfterUsername(username);
            UserDto userDto = UserDtoMapping.userToUserDtoComplet(user);
            return userDto;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("No User found with given Id", "msg - 006");
        }
    }

    public List<Permission> getUserPermissionsByUsername(String username) {
        return userRepo.findUserPermissions(username);
    }

    public List<UserDto> getAllUser() {
        List<User> userList = userRepo.getAllUser();
        List<UserDto> userDtoList = userList.stream().map(UserDtoMapping::userToUserDtoIncomplet).collect(Collectors.toList());
        return userDtoList;
    }

    //todo maybe get username instead of id
    public List<BugDto> getAllCreatedBugs(Integer id) throws BusinessException {
        try {
            User user = userRepo.findUser(id);
            List<Bug> bugList = userRepo.getAllCreatedBugs(user);
            List<BugDto> bugDtoList = bugList.stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
            return bugDtoList;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("No User found with given Id", "msg - 006");
        }
    }

    //todo maybe get username instead of id
    public List<BugDto> getAllAssignedBugs(Integer id) throws BusinessException {
        try {
            User user = userRepo.findUser(id);
            List<Bug> bugList = userRepo.getAllAssignedBugs(user);
            List<BugDto> bugDtoList = bugList.stream().map(BugDtoMapping::bugToBugDtoComplet).collect(Collectors.toList());
            return bugDtoList;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("No User found with given Id", "msg - 006");
        }
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

    public UserDto login(UserDto userDto) throws BusinessException {
        String encriptedPassword = Hashing.sha256().hashString(userDto.getPassword(), StandardCharsets.UTF_8).toString();
        User user;
        try {
            user = userRepo.login(userDto.getUsername(), encriptedPassword);
            userRepo.resetLoginFailCounter(user);
        } catch (BusinessException ex) {
            passwordFailed(userDto.getUsername());
            throw ex;
        }
        return UserDtoMapping.userToUserDtoIncomplet(user);
    }

    public void passwordFailed(String username) throws BusinessException {
        User user = userRepo.findeUserAfterUsername(username);
        if (user.getCounter() < 5) {
            user.setCounter(user.getCounter() + 1); //todo do in repo increse counter
            if (user.getCounter() == 5) {
                user.setStatus(false);
                throw new BusinessException("Password failed too may times, User deactivated", "msg - 003");
            }
        } else {
            throw new BusinessException("User Inactiv", "msg - 005");
        }
    }

    public UserDto activateUser(String username) throws BusinessException {
        User user = userRepo.findeUserAfterUsername(username);
        if (user.getStatus()) {
            throw new BusinessException("User already activ", "msg - 007");
        } else {
            userRepo.activateUser(user);
            return UserDtoMapping.userToUserDtoIncomplet(user);
        }
    }


    public UserDto deactivateUser(String username) throws BusinessException {
        User user = userRepo.findeUserAfterUsername(username);
        if (!user.getStatus()) {
            throw new BusinessException("User already deactivated", "msg - 008");
        }
        if (user.getAssignedTo() != null && !user.getAssignedTo().isEmpty()) {
            for (Bug bug : user.getAssignedTo()) {
                if (!bug.getStatus().equals(Bug.Status.CLOSED))
                    throw new BusinessException("User still has unclosed bugs", "msg - 009");
            }
        }
        userRepo.deactivateUser(user);
        return UserDtoMapping.userToUserDtoIncomplet(user);

    }


    //This function delete a User and all related activities(Notification, Bugs, Comments) and should
    // not be used in the context of the specification instead user the deactivateUser function.
    public Integer deleteUser(UserDto userDto) throws BusinessException {
        return userRepo.deleteUserAfterUserNamePermanently(userDto.getUsername());
    }

    //toDo validate data
    public UserDto updateUser(UserDto userDto) throws BusinessException {
        Validator.validateUser(userDto);
        User newDataUser = UserDtoMapping.userDtoToUser(userDto);
        return UserDtoMapping.userToUserDtoIncomplet(userRepo.updateUser(newDataUser));
    }

    public void addRoleToUser(UserDto userDto, RoleDto roleDto)
    {
        Role role = roleRepo.findRole(roleDto.getId());
        userRepo.addRoleToUser(UserDtoMapping.userDtoToUser(userDto),role);
    }

    public void addRolesToUser(UserDto userDto, List<RoleDto> rolesDto) {
        for (RoleDto roleDto : rolesDto) {
            addRoleToUser(userDto, roleDto);
        }
    }
}
