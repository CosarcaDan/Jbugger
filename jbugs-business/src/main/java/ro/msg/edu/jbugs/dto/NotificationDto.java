package ro.msg.edu.jbugs.dto;

import java.sql.Timestamp;

/**
 * Document me.
 *
 * @author msg systems AG; String Name.
 * @since 19.1.2
 */
public class NotificationDto {
    private Integer id;
    private Timestamp date;
    private String message;
    private String type;
    private String url;
    private boolean isSeen;
    private String receiver;


    public NotificationDto(Integer id, Timestamp date, String message, String type, String url, boolean isSeen, String receiver) {
        this.id = id;
        this.date = date;
        this.message = message;
        this.type = type;
        this.url = url;
        this.isSeen = isSeen;
        this.receiver = receiver;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getUsername() {
        return receiver;
    }

    public void setUsername(String receiver) {
        this.receiver = receiver;
    }
}
