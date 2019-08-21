package ro.msg.edu.jbugs.interceptor;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class PathPolicy {
    private HashMap<String,List<String>> pathPermissions;

    public PathPolicy() {
        this.pathPermissions = new HashMap<>();
        List<String> perissions= new ArrayList<>();
        perissions.add("USER_MANAGEMENT");
        pathPermissions.put("users/ttoken",new ArrayList<>(perissions));

        perissions.clear();
        perissions.add("USER_MANAGEMENT");
        perissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("roles",new ArrayList<>(perissions));

        perissions.clear();
        perissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("roles/permissions", new ArrayList<>(perissions));

        pathPermissions.put("users/login",new ArrayList<>());

        perissions.clear();
        perissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("permissions", new ArrayList<>(perissions));

        perissions.clear();
        perissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("roles/add-permissions", new ArrayList<>(perissions));

        perissions.clear();
        perissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("roles/remove-permissions", new ArrayList<>(perissions));

        perissions.clear();
        perissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("permissions/not-in-role", new ArrayList<>(perissions));

        perissions.clear();
        perissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("permissions/in-role", new ArrayList<>(perissions));

        perissions.clear();
        perissions.add("USER_MANAGEMENT");
        pathPermissions.put("users",new ArrayList<>(perissions));
    }

    public HashMap<String, List<String>> getPathPermissions() {
        return pathPermissions;
    }

    public void setPathPermissions(HashMap<String, List<String>> pathPermissions) {
        this.pathPermissions = pathPermissions;
    }
}
