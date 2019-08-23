package ro.msg.edu.jbugs.dto;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class AttachmentDto implements Serializable {

    private Integer id;
    private String attContent;

    public AttachmentDto() {
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

    public static AttachmentDto fromString(String JSON) {
        Gson g = new Gson();
        AttachmentDto result = g.fromJson(JSON, AttachmentDto.class);
        return result;
    }

}
