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
//        @NamedQuery(name = User.GET_USER_PERMISSIONS, query = "Select  p\n" +
//                "From User u\n" +
//                "inner join u.roleList ur on u.id = ur.id\n" +
//                "inner join Role r on ur.id = r.id\n" +
//                "inner join r.permissionList rp on rp.id = r.id\n" +
//                "inner join Permission p on p.id = rp.id\n" +
//                "where u.id in (select us.id\n" +
//                "                from User us\n" +
//                "                where us.username = :username)")


})
public class User implements Serializable {
    public static final String QUERY_SELECT_ALL_USER = "getAllUser";
    public static final String QUERY_SELECT_AFTER_USERNAME = "findAfterUsername";
    public static final String QUERY_REMOVE_AFTER_USERNAME = "removeAfterUsername";
    public static final String QUERY_USER_LOGIN_AFTER_USERNAME_PASSWORD = "userLogin";
    public static final String QUERY_COUNT_USER_NAME_UNIQUE = "checkUserNameUnique";
//    public static final String GET_USER_PERMISSIONS = "get user permissions";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "counter")
    private Integer counter;

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
    private List<Bug> createdBy;

    @OneToMany(mappedBy = "assigned", cascade = CascadeType.MERGE)
    private List<Bug> assignedTo;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = {@JoinColumn(name= "role_id")})
    private List<Role> roleList;

    public User(Integer counter, String firstName, String lastName, String email, String mobileNumber, String password, String username, Boolean status) {
        this.counter = counter;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.username = username;
        this.status = status;
    }

    public User() {
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Bug> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(List<Bug> createdBy) {
        this.createdBy = createdBy;
    }

    public List<Bug> getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(List<Bug> assignedTo) {
        this.assignedTo = assignedTo;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(counter, user.counter) &&
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
        return Objects.hash(counter, firstName, lastName, email, mobileNumber, password, username, status);
    }

    public void addRole(Role role) {
        if (this.roleList.stream().anyMatch(role1 -> role1.getType().equals(role.getType())))
            return;

        role.addUserSimple(this);
        this.roleList.add(role);
    }

    public void deleteRole(Role role) {
        role.deleteUserSimple(this);
        this.roleList.remove(role);
    }
}
