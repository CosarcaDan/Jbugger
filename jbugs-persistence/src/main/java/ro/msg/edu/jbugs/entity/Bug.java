package ro.msg.edu.jbugs.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */

@Entity
@Table(name = "bugs")
@NamedQueries({
        @NamedQuery(name = Bug.GET_ALL_BUGS, query = "select b from Bug b"),
        @NamedQuery(name = Bug.DELETE_OLD_BUGS, query = "delete from Bug b where b.targetDate < :date"),
        @NamedQuery(name = Bug.FINDE_BUG_AFTER_USER_ID, query = "select b from Bug b where b.created = :user or b.assigned = :user"),
        @NamedQuery(name = Bug.DELETE_BUG_AFTER_USER_ID, query = "delete from Bug b where b.created = :user or b.assigned = :user")
})
public class Bug implements Serializable {

    public static final String GET_ALL_BUGS = "getAllBugs";
    public static final String DELETE_OLD_BUGS = "deleteOldBugs";
    public static final String DELETE_BUG_AFTER_USER_ID = "deleteBugAfterUserId";
    public static final String FINDE_BUG_AFTER_USER_ID = "Finde_BUG_AFTER_USER_ID";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Column(name = "targetDate")
    private Timestamp targetDate;


    //ToDo verify if enum works with DB
    @Column(name = "status")
    private Status status;

    @Column(name = "fixedVersion")
    private String fixedVersion;

    @Column(name = "severity")
    private Severity severity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "CREATED_ID")
    private User created;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ASSIGNED_ID")
    private User assigned;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_bug")
    private List<Attachment> attachments;

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Timestamp getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Timestamp targetDate) {
        this.targetDate = targetDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFixedVersion() {
        return fixedVersion;
    }

    public void setFixedVersion(String fixedVersion) {
        this.fixedVersion = fixedVersion;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    //todo posibil doar numele -> scapam de userDto incomplet prin eliminarea buclei infinite
    public User getCreated() {
        return created;
    }

    public void setCreated(User created) {
        this.created = created;
    }

    //todo posibil doar numele
    public User getAssigned() {
        return assigned;
    }

    public void setAssigned(User assigned) {
        this.assigned = assigned;
    }

    public enum Status{
        NEW, IN_PROGRESS, FIXED, CLOSED, REJECTED, INFONEEDED
    }

    public enum Severity{
        CRITICAL, HIGH, MEDIUM, LOW
    }
}
