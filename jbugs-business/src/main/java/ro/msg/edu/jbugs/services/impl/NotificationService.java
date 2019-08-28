package ro.msg.edu.jbugs.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private Gson gson;

    public void addNotificationNewUser(User receiver) {
        gson = new GsonBuilder().create();
        //todo exclude password
        String welcomeMessage = gson.toJson(receiver);
        NotificationDto notificationDto = new NotificationDto(0,new Timestamp(System.currentTimeMillis()),welcomeMessage,"WELCOME_NEW_USER","",false,receiver.getUsername());
        Notification notification = NotificationDtoMapping.notificationDtoTonotification(notificationDto, receiver);
        notificationRepo.addNotification(notification);

    }

    public NotificationDto findNotification(Integer id){
        Notification notification = notificationRepo.findNotification(id);
        NotificationDto notificationDto = NotificationDtoMapping.notificationTonotificationDto(notification);
        return notificationDto;
    }

    public List <NotificationDto> findAllNotificationsByUsername(String username){
        List <Notification> notifications = notificationRepo.findAllNotificationsByUsername(username);
        return notifications.stream().map(NotificationDtoMapping::notificationTonotificationDto).collect(Collectors.toList());
    }


}
