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
@Table(name = "permissions")
@NamedQueries({
        @NamedQuery(name = Permission.GET_ALL_PERMISSIONS, query = "select p from Permission p")
})
public class Permission implements Serializable {
    public static final String GET_ALL_PERMISSIONS = "get all permissions";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "description")
    private String description;

    //    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private String type;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.MERGE)
    @JoinTable(name = "roles_permissions", joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = {@JoinColumn(name= "permission_id")})
    private List<Role> roleList;

    public Permission() {
    }

    public Permission(String description, String type, List<Role> roleList) {
        this.description = description;
        this.type = type;
        this.roleList = roleList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public void addRoleSimple(Role role) {
        this.roleList.add(role);
    }

    public void removeRoleSimple(Role role) {
        this.roleList.removeIf(r -> r.getId().equals(role.getId()));
    }
}
