package ro.msg.edu.jbugs.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Attachment is an entity mapped with the table Attachments
 * from the MySql msg_training database
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
@Entity
@Table(name = "attachments")
@NamedQueries({
        @NamedQuery(name = Attachment.DELETE_ATTACHMENTS_AFTER_BUG_ID, query = "delete from Attachment a where" +
                " a.bug = :bug "),
        @NamedQuery(name = Attachment.DELETE_ATTACHMENT_AFTER_ID, query = "delete from Attachment  a where a.id=:id")
})
public class Attachment implements Serializable {

    /**
     * Static fields for the possible queries on the attachment entity.
     */
    public static final String DELETE_ATTACHMENTS_AFTER_BUG_ID = "deleteAttachmentsAfterBugId";
    public static final String DELETE_ATTACHMENT_AFTER_ID = "deleteAttachmentAfterId";

    /**
     * Mapping the attachment's fields with the columns
     * of the attachment table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "attContent")
    private String attContent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_bug")
    private Bug bug;

    /**
     * Default constructor for the attachment entity.
     */
    public Attachment() {
    }

    /**
     * Constructor with parameters for the attachment entity.
     * @param attContent - string; the name of the attachment
     * @param bug - Bug; the bug that the attachment belongs to
     * */
    public Attachment(String attContent, Bug bug) {
        this.attContent = attContent;
        this.bug = bug;
    }

    /**
     * Getters and setters for each field of the attachment
     * entity.
     * */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttContent() {
        return attContent;
    }

    public void setAttContent(String attContent) {
        this.attContent = attContent;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }
}
