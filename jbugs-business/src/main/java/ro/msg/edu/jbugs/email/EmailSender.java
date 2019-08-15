package ro.msg.edu.jbugs.email;
/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailSender {
    private static final String senderEmail = "liverpoolanfield03641@gmail.com";//change with your sender email
    private static final String senderPassword = "159753stef";//change with your sender password

    public static void sendAsHtml(String to, String title, String html) throws MessagingException {
        System.out.println("Sending email to " + to);
        Session session = createSession();
        //create message using session
        MimeMessage message = new MimeMessage(session);
        prepareEmailMessage(message, to, title, html);
        //sending message
        Transport.send(message);
        System.out.println("Done");
    }
    public static void sendMail(String msg) throws Exception {
        Session session = createSession();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("liverpoolanfield03641@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse("cosarcadan@gmail.com"));
        message.setSubject("Bugs Report");
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        Transport.send(message);
    }
    private static void prepareEmailMessage(MimeMessage message, String to, String title, String html)
            throws MessagingException {
        message.setContent(html, "text/html; charset=utf-8");
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(title);
    }
    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", "mail");
        props.put("mail.smtp.password", "pw123456!");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        return session;
    }

}