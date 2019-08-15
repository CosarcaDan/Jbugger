package ro.msg.edu.jbugs.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class UserDto implements Serializable {
    private Integer id;
    private Integer counter;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String password;
    private String username;
    private Boolean status;
    private List<BugDto> createdBy;
    private List<BugDto> assignedTo;

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

    public List<BugDto> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(List<BugDto> createdBy) {
        this.createdBy = createdBy;
    }

    public List<BugDto> getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(List<BugDto> assignedTo) {
        this.assignedTo = assignedTo;
    }

    public UserDto() {
    }

    public UserDto(Integer id, Integer counter, String firstName, String lastName, String email, String mobileNumber, String password, String username, Boolean status,List<BugDto> createdBy,List<BugDto> assignTo) {
        this.id = id;
        this.counter = counter;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.username = username;
        this.status = status;

        this.createdBy = createdBy;
        this.assignedTo = assignTo;
    }

    public UserDto(Integer id, Integer counter, String firstName, String lastName, String email, String mobileNumber, String password, String username, Boolean status) {
        this.id = id;
        this.counter = counter;
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

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
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


    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", counter=" + counter +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", createdBy=" + createdBy +
                ", assignedTo=" + assignedTo +
                '}';
    }
}
