package ro.msg.edu.jbugs.dto;

import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Creates an entity of type BugDto
 * that can be seen by the client.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
public class BugDto implements Serializable {

    private Integer id;
    private String title;
    private String description;
    private String version;
    private Timestamp targetDate;
    private String status;
    private String fixedVersion;
    private String severity;
    private String created;
    private String assigned;

    /**
     * Default constructor that creates the
     * entity.
     */
    public BugDto() {
    }

    /**
     * Constructor with parameters for creating a new bug enity.
     */
    public BugDto(Integer id, String title, String description, String version,
                  Timestamp targetDate, String status, String fixedVersion,
                  String severity, String created, String assigned) {
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

    /**
     * Getters and setters for each field
     * of the bugDto.
     */

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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
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
                ", created=" + created +
                ", assigned=" + assigned +
                '}';
    }

    public static BugDto fromString(String JSON) {
        Gson g = new Gson();
        BugDto result = g.fromJson(JSON, BugDto.class);
        return result;
    }
}
