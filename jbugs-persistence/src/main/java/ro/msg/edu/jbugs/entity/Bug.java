package ro.msg.edu.jbugs.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Bug is an entity mapped with the table Bugs from the
 * MySql msg_training database.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
@Entity
@Table(name = "bugs")
@NamedQueries({
        @NamedQuery(name = Bug.GET_ALL_BUGS, query = "select b from Bug b"),
        @NamedQuery(name = Bug.DELETE_OLD_BUGS, query = "delete from Bug b where b.targetDate < :date"),
        @NamedQuery(name = Bug.FIND_BUG_AFTER_USER_ID, query = "select b from Bug b where b.created = :user " +
                "or b.assigned = :user"),
        @NamedQuery(name = Bug.DELETE_BUG_AFTER_USER_ID, query = "delete from Bug b where b.created = :user or " +
                "b.assigned = :user"),
        @NamedQuery(name = Bug.SEARCH_CRITERIA_WITH_STATUS_AND_SEVERITY, query = "select b from Bug b where " +
                "b.status = :status and b.severity = :severity and b.created.username like :creator and" +
                " b.assigned.username like :assigned"),
        @NamedQuery(name = Bug.SEARCH_CRITERIA_WITH_STATUS, query = "select b from Bug b where " +
                "b.status = :status and b.created.username like :creator and b.assigned.username like :assigned"),
        @NamedQuery(name = Bug.SEARCH_CRITERIA_WITH_SEVERITY, query = "select b from Bug b where " +
                "b.severity = :severity and b.created.username like :creator and b.assigned.username like :assigned"),
        @NamedQuery(name = Bug.SEARCH_CRITERIA_WITHOUT_STATUS_AND_SEVERITY, query = "select b from Bug b where " +
                "b.created.username like :creator and b.assigned.username like :assigned"),
})
public class Bug implements Serializable {

    /**
     * Static fields for the possible queries on the bug entity.
     */
    public static final String GET_ALL_BUGS = "getAllBugs";
    public static final String DELETE_OLD_BUGS = "deleteOldBugs";
    public static final String DELETE_BUG_AFTER_USER_ID = "deleteBugAfterUserId";
    public static final String FIND_BUG_AFTER_USER_ID = "findBugAfterUserId";
    public static final String SEARCH_CRITERIA_WITH_STATUS_AND_SEVERITY = "searchCriteriaWithStatusAndSeverity";
    public static final String SEARCH_CRITERIA_WITH_STATUS = "searchCriteriaWithStatus";
    public static final String SEARCH_CRITERIA_WITH_SEVERITY = "searchCriteriaWithSeverity";
    public static final String SEARCH_CRITERIA_WITHOUT_STATUS_AND_SEVERITY = "searchCriteriaWithOutStatusAndSeverity";

    /**
     * Mapping the bug's fields with the columns
     * of the bug table.
     */
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

    /**
     * Getters and setters for each field of the bug
     * entity.
     */

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

    public User getCreated() {
        return created;
    }

    public void setCreated(User created) {
        this.created = created;
    }

    public User getAssigned() {
        return assigned;
    }

    public void setAssigned(User assigned) {
        this.assigned = assigned;
    }

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    /**
     * Removes the attachment of a bug after the given id
     * of the attachment if the id exists.
     * @param id - the Id of the attachment that has to be
     *           deleted
     * */
    public void removeAttachmentAfterId(Integer id) {
        this.attachments.removeIf(attachment -> attachment.getId().equals(id));
    }

    /**
     * The bug can take 6 possible statuses: new, in progress, fixed,
     * closed, rejected or info needed. All these statuses are encapsulated
     * in the Status enumeration.
     * */
    public enum Status{
        NEW, IN_PROGRESS, FIXED, CLOSED, REJECTED, INFONEEDED
    }

    /**
     * The bug can have 4 possible severities: critical, high,
     * medium or low. All these severities are encapsulated
     * in the Severity enumeration.
     * */
    public enum Severity{
        CRITICAL, HIGH, MEDIUM, LOW
    }
}
