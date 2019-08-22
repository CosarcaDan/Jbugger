package ro.msg.edu.jbugs.dto;

import com.google.gson.Gson;

public class RoleDto {
    private Integer id;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static RoleDto fromString(String JSON) {
        Gson g = new Gson();
        RoleDto result = g.fromJson(JSON, RoleDto.class);
        return result;
    }
}
