package ro.msg.edu.jbugs.dto;

import ro.msg.edu.jbugs.dto.UserDto;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Document me.
 *
 * @author msg systems AG; UserDto Name.
 * @since 19.1.2
 */
public class NotificationDto {
    private Integer id;
    private Timestamp date;
    private String message;
    private String type;
    private String url;
    private UserDto userDto;

    public NotificationDto(Integer id, Timestamp date, String message, String type, String url, UserDto userDto) {
        this.id = id;
        this.date = date;
        this.message = message;
        this.type = type;
        this.url = url;
        this.userDto = userDto;
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

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
