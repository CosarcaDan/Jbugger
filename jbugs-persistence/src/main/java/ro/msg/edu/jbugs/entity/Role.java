package ro.msg.edu.jbugs.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "type")
    private String type;

    @ManyToMany(targetEntity = Permission.class, mappedBy = "roleList",cascade = CascadeType.MERGE)
    private List<Permission> permissionList;


    @ManyToMany(targetEntity = User.class, mappedBy = "roleList", cascade = CascadeType.MERGE)
    private List<User> userList;

    public Role() {
    }

    public Role(String type, List<Permission> permissionList, List<User> userList) {
        this.type = type;
        this.permissionList = permissionList;
        this.userList = userList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void addPermission(Permission permission) {
        permission.addRoleSimple(this);
        this.permissionList.add(permission);
    }

    public void addUserSimple(User user) {
        this.userList.add(user);
    }
}
