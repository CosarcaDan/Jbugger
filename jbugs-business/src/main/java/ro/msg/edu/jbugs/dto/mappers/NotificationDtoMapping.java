package ro.msg.edu.jbugs.dto.mappers;

import ro.msg.edu.jbugs.dto.NotificationDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.entity.Notification;
import ro.msg.edu.jbugs.entity.User;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class NotificationDtoMapping {
    public static Notification notificationDtoTonotification(NotificationDto notificationDto, User receiver) {
        Notification notification = new Notification();
        notification.setId(notificationDto.getId());
        notification.setDate(notificationDto.getDate());
        notification.setMessage(notificationDto.getMessage());
        notification.setType(Notification.NotificationType.valueOf(notificationDto.getType()));
        notification.setUrl(notificationDto.getUrl());
        notification.setUser(receiver);
        return notification;
    }

    public static NotificationDto notificationTonotificationDto(Notification notification) {
        NotificationDto notificationDto = new NotificationDto(notification.getId(),notification.getDate(),notification.getMessage(),notification.getType().toString(),notification.getUrl(),notification.isSeen(),notification.getUser().getUsername());
        return notificationDto;
    }
}
