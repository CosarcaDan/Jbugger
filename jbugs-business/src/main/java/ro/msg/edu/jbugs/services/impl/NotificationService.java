package ro.msg.edu.jbugs.services.impl;

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

    public void addNotification(NotificationDto notificationDto) throws IOException {
        Notification notification = NotificationDtoMapping.notificationDtoTonotification(notificationDto);
        notificationRepo.addNotification(notification);
        sendNotification("hello new User: " + notification.getUser().getUsername());

    }

    public NotificationDto findNotification(Integer id){
        Notification notification = notificationRepo.findNotification(id);
        NotificationDto notificationDto = NotificationDtoMapping.notificationTonotificationDto(notification);
        return notificationDto;
    }

    //todo what?
    private void sendNotification(String text) throws IOException {
        //maby try with resource? or finally
        try {
            Context ic = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ic.lookup("java:comp/DefaultJMSConnectionFactory");
            Queue queue = (Queue) ic.lookup("tutorialQueue");

            Connection connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer publisher = session.createProducer(queue);
            connection.start();

            TextMessage message = session.createTextMessage(text);
            publisher.send(message);

        } catch (NamingException | JMSException e) {
            System.out.println("Error while trying to send <" + text + "> message: " + e.getMessage());
        }

        System.out.println("Message sent: " + text);
    }
}
