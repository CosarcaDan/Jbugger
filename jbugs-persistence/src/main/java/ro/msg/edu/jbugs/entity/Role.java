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
@NamedQueries({
        @NamedQuery(name = Role.GET_ALL_ROLES, query = "select r from Role r"),
        @NamedQuery(name = Role.GET_PERMISSIONS_BY_ROLE, query = " select p from Role r " +
                "inner join r.permissionList p on r.id = p.id where r.id = :id"),
})
public class Role implements Serializable {
    public static final String GET_ALL_ROLES = "get all roles";
    public static final String GET_PERMISSIONS_BY_ROLE = "get permissions by role";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "type")
    private String type;
    @ManyToMany(targetEntity = Permission.class, mappedBy = "roleList",cascade = CascadeType.MERGE)
    private List<Permission> permissionList;

    @ManyToMany(targetEntity = User.class, mappedBy = "roles", cascade = CascadeType.MERGE)
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

    public void deleteUserSimple(User user) {
        this.userList.remove(user);
    }

    public void removePermission(Permission permission) {
        permission.removeRoleSimple(this);
        this.permissionList.removeIf(p -> p.getId().equals(permission.getId()));
    }

}
