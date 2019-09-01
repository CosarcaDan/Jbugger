package ro.msg.edu.jbugs.services.impl;

import com.google.common.hash.Hashing;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.NotificationDto;
import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.dto.mappers.BugDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.RoleDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.UserDtoMapping;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.entity.Role;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.exceptions.RepositoryException;
import ro.msg.edu.jbugs.repo.RoleRepo;
import ro.msg.edu.jbugs.repo.UserRepo;
import ro.msg.edu.jbugs.validators.Validator;

import javax.annotation.Nullable;
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

    @EJB
    private UserRepo userRepo;

    @EJB
    private RoleRepo roleRepo;

    @EJB
    private NotificationService notificationService;

    /**
     * Add the User created from the received UserDto if the data is Valid.
     * The new User has a default password (defaultPass) and the password is encrypted.
     * The new User has a automated generated unique username based on user's last and firstname
     *
     * @param userDto from which the user will be created
     * @return the created User
     * @throws BusinessException if the UserDto data is invalid
     */
    public User addUser(UserDto userDto) throws BusinessException {
        Validator.validateUser(userDto);
        userDto.setUsername(generateUserName(userDto.getLastName(), userDto.getFirstName()));
        String defaultPassword = "defaultPass";
        userDto.setPassword(Hashing.sha256().hashString(defaultPassword, StandardCharsets.UTF_8).toString());
        userDto.setFailedLoginAttempt(0);
        userDto.setStatus(true);
        User user = UserDtoMapping.userDtoToUser(userDto);
        User newUser = userRepo.addUser(user);
        UserDto newUserDto = UserDtoMapping.userToUserDtoWithoutBugId(newUser);
        notificationService.createNotificationNewUser(newUserDto);
        return newUser;
    }

    /**
     * Search a User with the given id
     *
     * @param id of the searched User
     * @return UserDto of the found User
     * @throws BusinessException if no User with the given id is found
     */
    public UserDto findUser(Integer id) throws BusinessException {
        try {
            User user = userRepo.findUser(id);
            UserDto userDto = UserDtoMapping.userToUserDtoWithoutBugId(user);
            return userDto;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("No User Found with the given id", "msg - 011");
        }
    }

    /**
     * Search a User with the given username
     *
     * @param username of the searched User
     * @return UserDto of the found User
     * @throws BusinessException if no User with the given username is found
     */
    public UserDto findUser(String username) throws BusinessException {
        try {
            User user = userRepo.findUserByUsername(username);
            UserDto userDto = UserDtoMapping.userToUserDtoWithoutBugId(user);
            return userDto;
        } catch (EntityNotFoundException | RepositoryException ex) {
            throw new BusinessException("User with the given Username was not found!", "msg - 019");
        }
    }

    /**
     * Gets all Permission of a User with the given username
     *
     * @param username of the User
     * @return List of Permission of the User with the given username
     * @throws BusinessException if no User with the given username is found
     */
    public List<Permission> getUserPermissionsByUsername(String username) throws BusinessException {
        try {
            return userRepo.findUserPermissions(username);
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Returns a list containing all Users
     *
     * @return a list containing all Users
     */
    public List<UserDto> getAllUser() {
        List<User> userList = userRepo.findAllUser();
        List<UserDto> userDtoList = userList.stream().map(UserDtoMapping::userToUserDtoWithoutBugId).collect(Collectors.toList());
        return userDtoList;
    }

    /**
     * Gets all Bugs created by a User with the given id
     *
     * @param id of the User
     * @return List of Bugs created by the User with the given id
     * @throws BusinessException if no User with the given username is found
     */
    public List<BugDto> getAllCreatedBugs(Integer id) throws BusinessException {
        try {
            User user = userRepo.findUser(id);
            List<Bug> bugList = userRepo.findAllCreatedBugs(user);
            List<BugDto> bugDtoList = bugList.stream().map(BugDtoMapping::bugToBugDtoComplete).collect(Collectors.toList());
            return bugDtoList;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("No User Found with the given id", "msg - 011");
        }
    }

    /**
     * Gets all Bugs assigned to a User with the given id
     *
     * @param id of the User
     * @return List of Bugs assigned to the User with the given id
     * @throws BusinessException if no User with the given username is found
     */
    public List<BugDto> getAllAssignedBugs(Integer id) throws BusinessException {
        try {
            User user = userRepo.findUser(id);
            List<Bug> bugList = userRepo.findAllAssignedBugs(user);
            List<BugDto> bugDtoList = bugList.stream().map(BugDtoMapping::bugToBugDtoComplete).collect(Collectors.toList());
            return bugDtoList;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("No User Found with the given id", "msg - 011");
        }
    }

    /**
     * Gets all Roles of a User with the given id
     *
     * @param id of the User
     * @return List of Roles of the User with the given id
     * @throws BusinessException if no User with the given username is found
     */
    public List<RoleDto> getAllRoles(Integer id) throws BusinessException {
        try {
            User user = userRepo.findUser(id);
            List<Role> roles = userRepo.getAllRoles(user);
            List<RoleDto> bugsDto = roles.stream().map(RoleDtoMapping::roleToRoleDto).collect(Collectors.toList());
            return bugsDto;
        } catch (EntityNotFoundException ex) {
            throw new BusinessException("No User Found with the given id", "msg - 011");
        }
    }

    /**
     * Generates a Username unique based on user first and last name.
     * Username is constructed as followed:
     * fist 5 letters of the lastname(if the lastname is shorted than 5 characters then it will be the complete lastname)
     * from the first name one character will be added, if the username is not unique another letter from the firstname
     * will be added, if all the letters from the username have been added, but the username is still not unique then
     * numbers will be added until the username will be unique
     *
     * @param lastname  of the user
     * @param firstname of the user
     * @return an unique username
     */
    public String generateUserName(String lastname, String firstname) {
        String firstPart;
        if (lastname.length() >= 5) {
            firstPart = lastname.substring(0, 5);
        } else {
            firstPart = lastname;
        }
        String username = firstPart.toLowerCase();
        String tempUsername;
        int nr = 0;
        int charPosition = 0;
        do {
            username = (username + firstname.charAt(charPosition++)).toLowerCase();
        } while (!userRepo.isUsernameUnique(username) && charPosition < firstname.length());
        if (userRepo.isUsernameUnique(username))
            return username;
        do {
            tempUsername = username + (nr++);

        } while (!userRepo.isUsernameUnique(tempUsername));

        return tempUsername;

    }

    /**
     * Verifies if a User is able to login with the data provided trough the UserDto
     *
     * @param userDto containing username and password
     * @return the UserDto of the logged User
     * @throws BusinessException if Username was unable to log in (from this method an exception is thrown if the login
     *                           was unsuccessful but the User han not yet reached the FailedLoginAttempts limit)
     */
    public UserDto login(UserDto userDto) throws BusinessException {
        String encriptedPassword = Hashing.sha256().hashString(userDto.getPassword(), StandardCharsets.UTF_8).toString();
        User user;
        try {
            user = userRepo.findByUsernameAndPassword(userDto.getUsername(), encriptedPassword);
            userRepo.setFailedLoginAttemptToZero(user);
        } catch (RepositoryException ex) {
            passwordFailed(userDto.getUsername());
            throw new BusinessException(ex);
        }
        return UserDtoMapping.userToUserDtoWithoutBugId(user);
    }

    /**
     * Method call in case of a failed Login, verifies if the User has to many failed Login attempts or is deactivated.
     * if not it increases the failed Login attempts counter and deactivates the User if the limit is exceeded
     *
     * @param username of the User that entered the wrong password
     * @throws BusinessException if password if entered wrong to many times or if the user is inactive
     *                           or if there is no username with the given username
     */
    public void passwordFailed(String username) throws BusinessException {
        try {
            User user = userRepo.findUserByUsername(username);
            if (user.getFailedLoginAttempt() < 5 && user.getStatus()) {
                user.setFailedLoginAttempt(user.getFailedLoginAttempt() + 1);
                if (user.getFailedLoginAttempt() == 5) {
                    user.setStatus(false);
                    throw new BusinessException("Password failed too may times, User deactivated", "msg - 003");
                }
            } else {
                throw new BusinessException("User Inactive", "msg - 002");
            }
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Updates a deactivated User's status to active
     *
     * @param username of the User to be activated
     * @return the activated User
     * @throws BusinessException if there is no User with the given Username or the User is already active
     */
    public UserDto activateUser(String username) throws BusinessException {
        try {
            User user = userRepo.findUserByUsername(username);
            if (user.getStatus()) {
                throw new BusinessException("User already active", "msg - 004");
            } else {
                userRepo.setStatusTrue(user);
                return UserDtoMapping.userToUserDtoWithoutBugId(user);
            }
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Updates a active User's status to deactivated and also creates Notification USER_DELETED and USER_DEACTIVATED
     *
     * @param username of the user to be deactivated
     * @param login    if this method is user in by the login(currently not the case)
     * @return the deactivated User
     * @throws BusinessException if there is no user with the given username, the User is already deactivated or if he
     *                           still has unclosed Bugs assigned to him
     */
    public UserDto deactivateUser(String username, @Nullable Boolean login) throws BusinessException {
        try {
            User user = userRepo.findUserByUsername(username);
            if (!user.getStatus()) {
                throw new BusinessException("User already deactivated", "msg - 005");
            }
            if (user.getAssignedBugs() != null && !user.getAssignedBugs().isEmpty() && (login != null && !login)) {
                for (Bug bug : user.getAssignedBugs()) {
                    if (!bug.getStatus().equals(Bug.Status.CLOSED))
                        throw new BusinessException("User still has unclosed bugs", "msg - 010");
                }
            }
            userRepo.setStatusFalse(user);
            UserDto deactivatedUserDto = UserDtoMapping.userToUserDtoWithoutBugId(user);
            if (roleRepo.findAdminRole() != null && !roleRepo.findAdminRole().getUserList().isEmpty()) {
                List<User> admins = roleRepo.findAdminRole().getUserList();
                List<UserDto> adminsDto = admins.stream().map(UserDtoMapping::userToUserDtoWithoutBugId).collect(Collectors.toList());
                notificationService.createNotificationDeactivateUserForAdmin(adminsDto, deactivatedUserDto);
            }

            notificationService.createNotificationDeactivateUserForUserManagement(
                    userRepo.findAllUser().stream().filter(user1 ->
                    {
                        try {
                            return userRepo.findUserPermissions(user1.getUsername()).stream().anyMatch(permission -> permission.getType().equals("USER_MANAGEMENT"));
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }).map(UserDtoMapping::userToUserDtoWithoutBugId).collect(Collectors.toList())
                    , deactivatedUserDto);
            return deactivatedUserDto;
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }

    }




    /**
     * This function delete a User and all related activities(Notification, Bugs, Comments) and should
     * not be used in the context of the specification instead user the setStatusFalse function.
     *
     * @param userDto to be permanently deleted
     * @return -1 if the delete has failed 1 if the user was successfully deleted
     * @throws BusinessException
     */
    public Integer deleteUser(UserDto userDto) throws BusinessException {
        try {
            return userRepo.deleteUserByUserNamePermanently(userDto.getUsername());
        } catch (RepositoryException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * Updates a User if the data is valid
     *
     * @param userDto of the with the new Data and id of the User to be updated
     * @return the updated User
     * @throws BusinessException if the new Data is invalid
     */
    public UserDto updateUser(UserDto userDto) throws BusinessException {
        try {
            Validator.validateUser(userDto);
            //userDto.setPassword(Hashing.sha256().hashString(userDto.getPassword(), StandardCharsets.UTF_8).toString());
            List<RoleDto> roleDtos = getAllRoles(userDto.getId());
            User newDataUser = UserDtoMapping.userDtoToUser(userDto);
            newDataUser.setRoles(roleDtos.stream().map(roleDto ->
            {
                Role res = roleRepo.findRole(roleDto.getId());
                res.addUserSimple(newDataUser);
                return res;
            }).collect(Collectors.toList()));
            User response = userRepo.updateUser(newDataUser);

            return UserDtoMapping.userToUserDtoWithoutBugId(response);
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Adds a Role to a User
     *
     * @param userDto of the User
     * @param roleDto of the Role
     */
    public void addRoleToUser(UserDto userDto, RoleDto roleDto) {
        Role role = roleRepo.findRole(roleDto.getId());
        userRepo.addRoleToUser(UserDtoMapping.userDtoToUser(userDto), role);
    }

    /**
     * Deletes a Role from a User
     *
     * @param userDto of the User
     * @param roleDto of the Role
     */
    public void deleteRoleFromUser(UserDto userDto, RoleDto roleDto) {
        Role role = roleRepo.findRole(roleDto.getId());
        userRepo.deleteRoleFromUser(UserDtoMapping.userDtoToUser(userDto), role);
    }

    /**
     * Checks if a user only has closed bugs assigned to him
     *
     * @param userDto User
     * @return true if a user only has closed bugs assigned to him, false otherwise
     * @throws BusinessException if there is no User with the given id
     */
    public boolean hasOnlyClosedBugs(UserDto userDto) throws BusinessException {
        List<BugDto> bugs = getAllAssignedBugs(userDto.getId());
        for (BugDto bug : bugs) {
            if (!bug.getStatus().equals(3))
                return false;
        }
        return true;
    }

    /**
     * Updates a User including the Roles if the data is valid and creates Notification for USER_UPDATED
     *
     * @param userDto         with the new data, and id of the user to be updated
     * @param usernameLogedIn of the user who initiated the Update
     * @param roleDtos        List of the new Roles
     * @return UserDto with the Modified User
     * @throws BusinessException if the new Data is invalid
     */
    public UserDto updateWithRoles(UserDto userDto, String usernameLogedIn, List<RoleDto> roleDtos) throws BusinessException {
        Validator.validateUser(userDto);
        User newDataUser = UserDtoMapping.userDtoToUser(userDto);
        newDataUser.setRoles(roleDtos.stream().map(roleDto ->
        {
            Role res = roleRepo.findRole(roleDto.getId());
            res.addUserSimple(newDataUser);
            return res;
        }).collect(Collectors.toList()));
        User res = null;
        try {
            UserDto initiator = UserDtoMapping.userToUserDtoWithoutBugId(userRepo.findUserByUsername(usernameLogedIn));
            UserDto userToBeUpdated = UserDtoMapping.userToUserDtoWithoutBugId(userRepo.findUserByUsername(userDto.getUsername()));
            UserDto oldUserData = new UserDto(0, userToBeUpdated.getFailedLoginAttempt(), userToBeUpdated.getFirstName(), userToBeUpdated.getLastName(), userToBeUpdated.getEmail(), userToBeUpdated.getMobileNumber(), userToBeUpdated.getPassword(), userToBeUpdated.getUsername(), userToBeUpdated.getStatus());
            res = userRepo.updateUser(newDataUser);
            UserDto modifiedUserDt = UserDtoMapping.userToUserDtoWithoutBugId(res);
            notificationService.createNotificationUpdateUser(initiator, modifiedUserDt, oldUserData);
            return modifiedUserDt;
        } catch (RepositoryException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Gives a List of NotificationDto belonging to a User with the given username
     *
     * @param username of the User
     * @return List of NotificationDto belonging to a User with the given username
     */
    public List<NotificationDto> findAllNotificationByUsername(String username) {
        return notificationService.findAllNotificationsByUsername(username);
    }

    /**
     * Marks the notification with the given id as seen
     *
     * @param id of the Notification
     */
    public void seenNotification(int id) {
        notificationService.seen(id);
    }
}
