package ro.msg.edu.jbugs.dto;

import javax.ws.rs.FormParam;

public class UserLoginDto {
    @FormParam("username")
    private String username;
    @FormParam("password")
    private String password;

    public UserLoginDto(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public UserLoginDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
