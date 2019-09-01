package ro.msg.edu.jbugs.dto;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Creates an entity of type AttachmentDto
 * that can be seen by the client.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
public class AttachmentDto implements Serializable {

    private Integer id;
    private String attContent;

    /**
     * Default constructor that creates the
     * entity.
     */
    public AttachmentDto() {
    }

    /**
     * Constructor with parameters that creates a new
     * AttachmentDto entity.
     *
     * @param id         - Integer; the Id of the attachment
     * @param attContent - String; the name of
     *                   the attachment
     */
    public AttachmentDto(Integer id, String attContent) {
        this.id = id;
        this.attContent = attContent;
    }

    /**
     * Getters and setters for each field
     * of the attachmentDto.
     */
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

    public static AttachmentDto fromString(String JSON) {
        Gson g = new Gson();
        AttachmentDto attachmentDto = g.fromJson(JSON, AttachmentDto.class);
        return attachmentDto;
    }
}
