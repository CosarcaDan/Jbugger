package ro.msg.edu.jbugs.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class BugDto implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private String version;
    private Date targetDate;
    private String status;
    private String fixedVersion;
    private String severity;
    private UserDto created;
    private UserDto assigned;

    public BugDto(Integer id, String title, String description, String version, Date targetDate, String status, String fixedVersion, String severity, UserDto created, UserDto assigned) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.version = version;
        this.targetDate = targetDate;
        this.status = status;
        this.fixedVersion = fixedVersion;
        this.severity = severity;
        this.created = created;
        this.assigned = assigned;
    }

    public BugDto() {
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

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFixedVersion() {
        return fixedVersion;
    }

    public void setFixedVersion(String fixedVersion) {
        this.fixedVersion = fixedVersion;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public UserDto getCreated() {
        return created;
    }

    public void setCreated(UserDto created) {
        this.created = created;
    }

    public UserDto getAssigned() {
        return assigned;
    }

    public void setAssigned(UserDto assigned) {
        this.assigned = assigned;
    }

    @Override
    public String toString() {
        return "BugDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", targetDate=" + targetDate +
                ", status='" + status + '\'' +
                ", fixedVersion='" + fixedVersion + '\'' +
                ", severity='" + severity + '\'' +
                ", created=" + created.getId() + " - "+created.getUsername()  +
                ", assigned=" + assigned.getId() +" - " + assigned.getUsername() +
                '}';
    }
}
