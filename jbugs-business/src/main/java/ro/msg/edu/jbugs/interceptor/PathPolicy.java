package ro.msg.edu.jbugs.interceptor;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class defines the Permissions needed for each operation.
 */
@Singleton
public class PathPolicy {
    private HashMap<String, List<String>> pathPermissions;

    public PathPolicy() {
        this.pathPermissions = new HashMap<>();
        pathPermissions.put("^users/log.*", new ArrayList<>());
        pathPermissions.put("^users/renew", new ArrayList<>());
        pathPermissions.put("^users/notifications", new ArrayList<>());
        pathPermissions.put("^users/notifications/.*/seen", new ArrayList<>());
        pathPermissions.put("^files/upload$", new ArrayList<>());
        pathPermissions.put("^files/download.*", new ArrayList<>());


        List<String> permissions = new ArrayList<>();
        permissions.add("USER_MANAGEMENT");
        pathPermissions.put("^users(?!/log)(?!/notifications.*)(?!/permissions)(?!/.*/get).*", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("USER_MANAGEMENT");
        pathPermissions.put("^users/changePassword", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("USER_MANAGEMENT");
        pathPermissions.put("^users/.*/get", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("USER_MANAGEMENT");
        pathPermissions.put("^users/permissions", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("USER_MANAGEMENT");
        permissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("^roles$", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("^permissions.*", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("BUG_MANAGEMENT");
        pathPermissions.put("^bugs", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("BUG_MANAGEMENT");
        pathPermissions.put("^users", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("BUG_EXPORT_PDF");
        pathPermissions.put("^bugs/getPDF", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("BUG_MANAGEMENT");
        pathPermissions.put("^bugs/attachments", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("BUG_MANAGEMENT");
        pathPermissions.put("^bugs/add", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("BUG_MANAGEMENT");
        pathPermissions.put("^bugs/delete-attachment", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("BUG_MANAGEMENT");
        pathPermissions.put("^bugs/[1234567890]+/.*", new ArrayList<>(permissions));

        permissions.clear();
        permissions.add("BUG_CLOSE");
        pathPermissions.put("^bugs/[1234567890]+", new ArrayList<>(permissions));


        permissions.clear();
        permissions.add("PERMISSION_MANAGEMENT");
        pathPermissions.put("^roles.*", new ArrayList<>(permissions));

    }

    public HashMap<String, List<String>> getPathPermissions() {
        return pathPermissions;
    }

    public void setPathPermissions(HashMap<String, List<String>> pathPermissions) {
        this.pathPermissions = pathPermissions;
    }
}
