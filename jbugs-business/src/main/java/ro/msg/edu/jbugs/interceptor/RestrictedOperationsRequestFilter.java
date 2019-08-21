package ro.msg.edu.jbugs.interceptor;

import ro.msg.edu.jbugs.TokenManager;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.services.impl.UserService;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Provider
public class RestrictedOperationsRequestFilter implements ContainerRequestFilter {

    @EJB
    UserService userService;

    @EJB
    PathPolicy pathPolicy;
    @Override
    public void filter(ContainerRequestContext ctx) {

        if (ctx.getMethod().equals("OPTIONS")) return;

        HashMap<String, List<String>> permissions = pathPolicy.getPathPermissions();
        String path = ctx.getUriInfo().getPath();
        if (!permissions.containsKey(path)) {
            ctx.abortWith(Response.status(Response.Status.NOT_FOUND)
                    .entity("Policy not found!")
                    .build());
            return;
        }
        List<String> permissionsRequired = permissions.get(path);

        if (permissionsRequired.size() == 0)
            return;
        else {

            String rawheader = ctx.getHeaderString("Authorization");
            if (rawheader == null) {
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authorization header missing!")
                        .build());
                return;
            } else {
                if (!rawheader.contains(" ")) {
                    if (rawheader.equals("")) {
                        ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                                .entity("Authorization header wrong format!")
                                .build());
                        return;
                    }
                }
                if (rawheader.equals("")) {
                    ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Authorization header missing!")
                            .build());
                    return;
                }
            }
            String header = rawheader.split(" ")[1];
            if (TokenManager.decodeJWT(header).getExpiration().toInstant().toEpochMilli() < Calendar.getInstance().getTime().toInstant().toEpochMilli()) {
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Auth Token Expired")
                        .build());
            }
            try {
                if (checkAccess(header, permissionsRequired))
                    return;
                else
                    ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Permissions missing")
                            .build());
            } catch (BusinessException e) {
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Permissions missing")
                        .build());
            }

        }


    }

    private boolean checkAccess(String token, List<String> permissions) throws BusinessException {
        String username = TokenManager.decodeJWT(token).getSubject();
        List<Permission> userPermissions = userService.getUserPermissionsByUsername(username);
        return permissions.stream().anyMatch(s -> {
            return userPermissions.stream().anyMatch(ss -> ss.getType().equals(s));
        });

    }
}
