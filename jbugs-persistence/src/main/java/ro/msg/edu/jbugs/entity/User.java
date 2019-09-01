package ro.msg.edu.jbugs.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = User.QUERY_SELECT_ALL_USER, query = "select u from User u"),
        @NamedQuery(name = User.QUERY_SELECT_AFTER_USERNAME, query = "select u from User u where u.username = :username"),
        @NamedQuery(name = User.QUERY_REMOVE_AFTER_USERNAME,query = "delete from User u where u.username = :username"),
        @NamedQuery(name = User.QUERY_USER_LOGIN_AFTER_USERNAME_PASSWORD, query = "select u from User u where u.username = :username and u.password = :password and u.status = 1"),
        @NamedQuery(name = User.QUERY_COUNT_USER_NAME_UNIQUE, query = "select count(u) from User u where u.username = :username "),
})
public class User implements Serializable {
    public static final String QUERY_SELECT_ALL_USER = "findAllUser";
    public static final String QUERY_SELECT_AFTER_USERNAME = "findAfterUsername";
    public static final String QUERY_REMOVE_AFTER_USERNAME = "removeAfterUsername";
    public static final String QUERY_USER_LOGIN_AFTER_USERNAME_PASSWORD = "userLogin";
    public static final String QUERY_COUNT_USER_NAME_UNIQUE = "checkUserNameUnique";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "counter")
    private Integer failedLoginAttempt;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "created", cascade = CascadeType.MERGE)
    private List<Bug> createdBugs;

    @OneToMany(mappedBy = "assigned", cascade = CascadeType.MERGE)
    private List<Bug> assignedBugs;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = {@JoinColumn(name= "role_id")})
    private List<Role> roles;

    public User() {
    }

    public User(Integer failedLoginAttempt, String firstName, String lastName, String email, String mobileNumber, String password, String username, Boolean status) {
        this.failedLoginAttempt = failedLoginAttempt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.username = username;
        this.status = status;
    }

    public void addRole(Role role) {
        if (this.roles.stream().anyMatch(role1 -> role1.getType().equals(role.getType())))
            return;
        role.addUserSimple(this);
        this.roles.add(role);
    }

    public void deleteRole(Role role) {
        role.deleteUserSimple(this);
        this.roles.remove(role);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roleList) {
        this.roles = roleList;
    }

    public List<Bug> getCreatedBugs() {
        return createdBugs;
    }

    public void setCreatedBugs(List<Bug> createdBy) {
        this.createdBugs = createdBy;
    }

    public List<Bug> getAssignedBugs() {
        return assignedBugs;
    }

    public void setAssignedBugs(List<Bug> assignedTo) {
        this.assignedBugs = assignedTo;
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFailedLoginAttempt() {
        return failedLoginAttempt;
    }

    public void setFailedLoginAttempt(Integer counter) {
        this.failedLoginAttempt = counter;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(failedLoginAttempt, user.failedLoginAttempt) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(mobileNumber, user.mobileNumber) &&
                Objects.equals(password, user.password) &&
                Objects.equals(username, user.username) &&
                Objects.equals(status, user.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(failedLoginAttempt, firstName, lastName, email, mobileNumber, password, username, status);
    }
}
