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

        pathPermissions.put("users/login",new ArrayList<>());
        pathPermissions.put("users",new ArrayList<>());
    }

    public HashMap<String, List<String>> getPathPermissions() {
        return pathPermissions;
    }

    public void setPathPermissions(HashMap<String, List<String>> pathPermissions) {
        this.pathPermissions = pathPermissions;
    }
}
