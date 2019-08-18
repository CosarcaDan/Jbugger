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
    public static Notification notificationDtoTonotification(NotificationDto notificationDto) {
        Notification notification = new Notification();
        notification.setId(notificationDto.getId());
        notification.setDate(notificationDto.getDate());
        notification.setMessage(notificationDto.getMessage());
        notification.setType(Notification.NotificationType.valueOf(notificationDto.getType()));
        notification.setUrl(notificationDto.getUrl());
        User user = UserDtoMapping.userDtoToUser(notificationDto.getUserDto());
        notification.setUser(user);
        return notification;
    }

    public static NotificationDto notificationTonotificationDto(Notification notification) {
        UserDto userDto = UserDtoMapping.userToUserDtoIncomplet(notification.getUser());
        NotificationDto notificationDto = new NotificationDto(notification.getId(),notification.getDate(),notification.getMessage(),notification.getType().toString(),notification.getUrl(),userDto);
        return notificationDto;
    }
}
