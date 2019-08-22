package ro.msg.edu.jbugs.dto;

import com.google.gson.Gson;

public class PermissionDto {
    private Integer id;
    private String description;
    private String type;

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

    public static PermissionDto fromString(String JSON) {
        Gson g = new Gson();
        PermissionDto result = g.fromJson(JSON, PermissionDto.class);
        return result;
    }
}
