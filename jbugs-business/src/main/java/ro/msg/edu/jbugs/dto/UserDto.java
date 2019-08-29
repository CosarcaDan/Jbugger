package ro.msg.edu.jbugs.dto;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
//@XmlRootElement
public class UserDto implements Serializable {
    private Integer id;
    private Integer failedLoginAttempt;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String password;
    private String username;
    private Boolean status;
    private List<Integer> createdBugsId;
    private List<Integer> assignedBugsId;
    private List<RoleDto> roles;


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Integer> getCreatedBugsId() {
        return createdBugsId;
    }

    public void setCreatedBugsId(List<Integer> createdBugsId) {
        this.createdBugsId = createdBugsId;
    }

    public List<Integer> getAssignedBugsId() {
        return assignedBugsId;
    }


    public void setAssignedBugsId(List<Integer> assignedBugsId) {
        this.assignedBugsId = assignedBugsId;
    }

    public UserDto() {
    }

    public UserDto(Integer id, Integer failedLoginAttempt, String firstName, String lastName, String email, String mobileNumber, String password, String username, Boolean status, List<Integer> createdBugs, List<Integer> assignTo) {
        this.id = id;
        this.failedLoginAttempt = failedLoginAttempt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.username = username;
        this.status = status;

        this.createdBugsId = createdBugs;
        this.assignedBugsId = assignTo;
    }

    public UserDto(Integer id, Integer failedLoginAttempt, String firstName, String lastName, String email, String mobileNumber, String password, String username, Boolean status, List<Integer> createdBugs, List<Integer> assignTo, List<RoleDto> roles) {
        this.id = id;
        this.failedLoginAttempt = failedLoginAttempt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.username = username;
        this.status = status;

        this.createdBugsId = createdBugs;
        this.assignedBugsId = assignTo;

        this.roles = roles;
    }

    public UserDto(UserLoginDto uld) {
        this.username = uld.getUsername();
        this.password = uld.getPassword();
    }

    public UserDto(Integer id, Integer failedLoginAttempt, String firstName, String lastName, String email, String mobileNumber, String password, String username, Boolean status) {
        this.id = id;
        this.failedLoginAttempt = failedLoginAttempt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.username = username;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFailedLoginAttempt() {
        return failedLoginAttempt;
    }

    public void setFailedLoginAttempt(Integer failedLoginAttempt) {
        this.failedLoginAttempt = failedLoginAttempt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) &&
                Objects.equals(failedLoginAttempt, userDto.failedLoginAttempt) &&
                Objects.equals(firstName, userDto.firstName) &&
                Objects.equals(lastName, userDto.lastName) &&
                Objects.equals(email, userDto.email) &&
                Objects.equals(mobileNumber, userDto.mobileNumber) &&
                Objects.equals(password, userDto.password) &&
                Objects.equals(username, userDto.username) &&
                Objects.equals(status, userDto.status) &&
                Objects.equals(createdBugsId, userDto.createdBugsId) &&
                Objects.equals(assignedBugsId, userDto.assignedBugsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, failedLoginAttempt, firstName, lastName, email, mobileNumber, password, username, status, createdBugsId, assignedBugsId);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", failedLoginAttempt=" + failedLoginAttempt +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", createdBugsId=" + createdBugsId +
                ", assignedBugsId=" + assignedBugsId +
                '}';
    }

    public static UserDto fromString(String JSON) {
        Gson g = new Gson();
        UserDto result = g.fromJson(JSON, UserDto.class);
        return result;
    }
}
