package ro.msg.edu.jbugs.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.NotificationDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.dto.mappers.NotificationDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.UserDtoMapping;
import ro.msg.edu.jbugs.entity.Notification;
import ro.msg.edu.jbugs.repo.NotificationRepo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Stateless
public class NotificationService {
    //Todo delete all old notification (see commentService)

    @EJB
    private NotificationRepo notificationRepo;

    /**
     * Creates a Notification for WELCOME_NEW_USER with the new Users Data
     *
     * @param receiver User to whom the Notification is addressed
     */
    public void createNotificationNewUser(UserDto receiver) {
        Gson gson = new GsonBuilder().create();
        String welcomeMessage = gson.toJson(receiver);
        NotificationDto notificationDto = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), welcomeMessage, "WELCOME_NEW_USER", "", false, receiver.getUsername());
        Notification notification = NotificationDtoMapping.notificationDtoTonotification(notificationDto, UserDtoMapping.userDtoToUser(receiver));
        notificationRepo.addNotification(notification);
    }

    /**
     * Creates a Notification for USER_UPDATED
     *
     * @param initiator       the User who updated another user
     * @param modified        the User who's data has been modified
     * @param modifiedOldData the old data of the User who's data has been modified
     */
    public void createNotificationUpdateUser(UserDto initiator, UserDto modified, UserDto modifiedOldData) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        List<UserDto> messageList = Arrays.asList(modified, modifiedOldData);
        String updateMessage = gson.toJson(messageList);
        NotificationDto notificationDtoForInitiator = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), updateMessage, "USER_UPDATED", "", false, initiator.getUsername());
        Notification notificationForInitiator = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForInitiator, UserDtoMapping.userDtoToUser(initiator));
        NotificationDto notificationDtoForModified = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), updateMessage, "USER_UPDATED", "", false, modified.getUsername());
        Notification notificationForModified = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForModified, UserDtoMapping.userDtoToUser(modified));
        notificationRepo.addNotification(notificationForInitiator);
        notificationRepo.addNotification(notificationForModified);
    }

    /**
     * Creates a Notification for USER_DEACTIVATED (for each User with the Admin Role)
     *
     * @param admins          List of Users with the Admin Role
     * @param deactivatedUser user who was deactivated
     */
    public void createNotificationDeactivateUserForAdmin(List<UserDto> admins, UserDto deactivatedUser) {
        Gson gson = new GsonBuilder().create();
        String deleteMessage = gson.toJson(deactivatedUser);
        for (UserDto admin : admins) {
            NotificationDto notificationDtoForAdmin = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), deleteMessage, "USER_DEACTIVATED", "", false, admin.getUsername());
            Notification notificationForAdmin = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForAdmin, UserDtoMapping.userDtoToUser(admin));
            notificationRepo.addNotification(notificationForAdmin);
        }
    }

    /**
     * Creates a Notification for USER_DELETED(for each User with the UserManagement Permission)
     *
     * @param usersWithUserManagementPermission List of Users with the UserManagement Permission
     * @param deactivatedUser                   user who was deactivated
     */
    public void createNotificationDeactivateUserForUserManagement(List<UserDto> usersWithUserManagementPermission,
                                                                  UserDto deactivatedUser) {
        Gson gson = new GsonBuilder().create();
        String deleteMessage = gson.toJson(deactivatedUser);
        for (UserDto userWithManagementPermission : usersWithUserManagementPermission) {
            NotificationDto notificationDtoForManagementPerm = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), deleteMessage, "USER_DELETED", "", false,
                    userWithManagementPermission.getUsername());
            Notification notificationForUserManagPerm = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForManagementPerm,
                    UserDtoMapping.userDtoToUser(userWithManagementPermission));
            notificationRepo.addNotification(notificationForUserManagPerm);
        }
    }

    /**
     * Creates a Notification for BUG_CLOSED
     *
     * @param bugCreator   User who created the Bug
     * @param bugAssignee  User to whom the user is currently assigned
     * @param closedBugDto the closed Bug
     */
    public void createNotificationCloseBug(UserDto bugCreator, UserDto bugAssignee, BugDto closedBugDto) {
        Gson gson = new GsonBuilder().create();
        String closeBugMessage = gson.toJson(closedBugDto);
        String url = "http://localhost:4200/dashboard/bugs/" + closedBugDto.getId();
        NotificationDto notificationDtoForBugCreator = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), closeBugMessage, "BUG_CLOSED", url, false, bugCreator.getUsername());
        Notification notificationForBugCreator = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForBugCreator, UserDtoMapping.userDtoToUser(bugCreator));
        notificationRepo.addNotification(notificationForBugCreator);
        if (!bugCreator.getUsername().equals(bugAssignee.getUsername())) {
            NotificationDto notificationDtoForBugAssignee = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), closeBugMessage, "BUG_CLOSED", url, false, bugAssignee.getUsername());
            Notification notificationForBugAssignee = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForBugAssignee, UserDtoMapping.userDtoToUser(bugAssignee));
            notificationRepo.addNotification(notificationForBugAssignee);
        }
    }

    /**
     * Creates a Notification for BUG_STATUS_UPDATED
     *
     * @param oldBugDto     old data of the Bug that was updated
     * @param newBugDataDto new data of the Bug that was updated
     * @param creatorDto    creator of the bug
     * @param assignedDto   User to whom the user is currently assigned
     */
    public void createNotificationBugEditOnlyStatus(BugDto oldBugDto, BugDto newBugDataDto, UserDto creatorDto, UserDto assignedDto) {
        Gson gson = new GsonBuilder().create();
        List<BugDto> bugDtos = Arrays.asList(oldBugDto, newBugDataDto);
        String updateMessage = gson.toJson(bugDtos);
        String url = "http://localhost:4200/dashboard/bugs/" + oldBugDto.getId();
        NotificationDto notificationDtoForBugCreator = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), updateMessage, "BUG_STATUS_UPDATED", url, false, creatorDto.getUsername());
        Notification notificationForBugCreator = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForBugCreator, UserDtoMapping.userDtoToUser(creatorDto));
        notificationRepo.addNotification(notificationForBugCreator);
        if (!creatorDto.getUsername().equals(assignedDto.getUsername())) {
            NotificationDto notificationDtoForBugAssignee = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), updateMessage, "BUG_STATUS_UPDATED", url, false, assignedDto.getUsername());
            Notification notificationForBugAssignee = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForBugAssignee, UserDtoMapping.userDtoToUser(assignedDto));
            notificationRepo.addNotification(notificationForBugAssignee);
        }
    }

    /**
     * Creates a Notification for BUG_UPDATED
     *
     * @param oldBugDto   old data of the Bug that was updated
     * @param updatedBug  new data of the Bug that was updated
     * @param creatorDto  creator of the bug
     * @param assignedDto User to whom the user is currently assigned
     */
    public void createNotificationBugEdit(BugDto oldBugDto, BugDto updatedBug, UserDto creatorDto, UserDto assignedDto) {
        Gson gson = new GsonBuilder().create();
        List<BugDto> bugDtos = Arrays.asList(oldBugDto, updatedBug);
        String updateMessage = gson.toJson(bugDtos);
        String url = "http://localhost:4200/dashboard/bugs/" + oldBugDto.getId();
        NotificationDto notificationDtoForBugCreator = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), updateMessage, "BUG_UPDATED", url, false, creatorDto.getUsername());
        Notification notificationForBugCreator = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForBugCreator, UserDtoMapping.userDtoToUser(creatorDto));
        notificationRepo.addNotification(notificationForBugCreator);
        if (!creatorDto.getUsername().equals(assignedDto.getUsername())) {
            NotificationDto notificationDtoForBugAssignee = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), updateMessage, "BUG_UPDATED", url, false, assignedDto.getUsername());
            Notification notificationForBugAssignee = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForBugAssignee, UserDtoMapping.userDtoToUser(assignedDto));
            notificationRepo.addNotification(notificationForBugAssignee);
        }
    }

    /**
     * Returns a NotificationDto of a Notification with the given id
     *
     * @param id of the Notification the be searched
     * @return NotificationDto of a Notification with the given id
     */
    public NotificationDto findNotification(Integer id) {
        Notification notification = notificationRepo.findNotification(id);
        NotificationDto notificationDto = NotificationDtoMapping.notificationTonotificationDto(notification);
        return notificationDto;
    }

    /**
     * Returns all Notifications belonging to a User
     *
     * @param username of the User which Notifications to return
     * @return List of NotificationDto's belonging to a User with the given username
     */
    public List<NotificationDto> findAllNotificationsByUsername(String username) {
        List<Notification> notifications = notificationRepo.findAllNotificationsByUsername(username);
        return notifications.stream().map(NotificationDtoMapping::notificationTonotificationDto).collect(Collectors.toList());
    }

    /**
     * Updated the isSeen field of a Notification to true (marks the Notification as seen)
     *
     * @param id of the Notification to be marked as seen
     */
    public void seen(Integer id) {
        Notification notification = notificationRepo.findNotification(id);
        notification.setSeen(true);
        notificationRepo.update(notification);
    }
}
