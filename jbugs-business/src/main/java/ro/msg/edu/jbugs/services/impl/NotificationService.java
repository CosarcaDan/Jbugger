package ro.msg.edu.jbugs.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.dto.mappers.UserDtoMapping;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.repo.NotificationRepo;
import ro.msg.edu.jbugs.dto.NotificationDto;
import ro.msg.edu.jbugs.dto.mappers.NotificationDtoMapping;
import ro.msg.edu.jbugs.entity.Notification;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    public void createNotificationNewUser(UserDto receiver) {
        Gson gson = new GsonBuilder().create();
        //todo exclude password
        String welcomeMessage = gson.toJson(receiver);
        NotificationDto notificationDto = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), welcomeMessage, "WELCOME_NEW_USER", "", false, receiver.getUsername());
        Notification notification = NotificationDtoMapping.notificationDtoTonotification(notificationDto, UserDtoMapping.userDtoToUser(receiver));
        notificationRepo.addNotification(notification);
    }

    public void createNotificationUpdateUser(UserDto initiator, UserDto modified, UserDto modifiedOldData) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        List<UserDto> messageList = Arrays.asList(modified, modifiedOldData);
        String updateMessage =  gson.toJson(messageList);
        NotificationDto notificationDtoForInitiator = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), updateMessage, "USER_UPDATED", "", false, initiator.getUsername());
        Notification notificationForInitiator = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForInitiator, UserDtoMapping.userDtoToUser(initiator));
        NotificationDto notificationDtoForModified = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), updateMessage, "USER_UPDATED", "", false, modified.getUsername());
        Notification notificationForModified = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForModified, UserDtoMapping.userDtoToUser(modified));
        notificationRepo.addNotification(notificationForInitiator);
        notificationRepo.addNotification(notificationForModified);
    }

    public void createNotificationDeactivateUserForAdmin(List<UserDto> admins,UserDto deactivatedUser){
        Gson gson = new GsonBuilder().create();
        String deleteMessage = gson.toJson(deactivatedUser);
        for (UserDto admin:admins){
            NotificationDto notificationDtoForAdmin = new NotificationDto(0, new Timestamp(System.currentTimeMillis()), deleteMessage, "USER_DEACTIVATED", "", false, admin.getUsername());
            Notification notificationForAdmin = NotificationDtoMapping.notificationDtoTonotification(notificationDtoForAdmin, UserDtoMapping.userDtoToUser(admin));
            notificationRepo.addNotification(notificationForAdmin);
        }

    }


    public void createNotificationDeactivateUserForUserManagement(){
        Gson gson = new GsonBuilder().create();


    }

    public NotificationDto findNotification(Integer id) {
        Notification notification = notificationRepo.findNotification(id);
        NotificationDto notificationDto = NotificationDtoMapping.notificationTonotificationDto(notification);
        return notificationDto;
    }

    public List<NotificationDto> findAllNotificationsByUsername(String username) {
        List<Notification> notifications = notificationRepo.findAllNotificationsByUsername(username);
        return notifications.stream().map(NotificationDtoMapping::notificationTonotificationDto).collect(Collectors.toList());
    }


}
