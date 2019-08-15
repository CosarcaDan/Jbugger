package ro.msg.edu.jbugs.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Entity
@Table(name = "attachments")
@NamedQueries({
        @NamedQuery(name = Attachment.DELETE_ATTACHMENTS_AFTER_BUG_ID, query = "delete from Attachment a where a.bug = :bug ")
})
public class Attachment implements Serializable {
    public static final String DELETE_ATTACHMENTS_AFTER_BUG_ID = "deleteAttachmentsAfterBugId";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "attContent")
    private String attContent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_bug")
    private Bug bug;

    public Attachment() {
    }

    public Attachment(String attContent, Bug bug) {
        this.attContent = attContent;
        this.bug = bug;
    }

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
