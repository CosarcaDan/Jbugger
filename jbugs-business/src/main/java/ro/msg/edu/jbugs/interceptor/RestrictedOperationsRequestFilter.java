package ro.msg.edu.jbugs.interceptor;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import ro.msg.edu.jbugs.TokenManager;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.services.impl.UserService;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This Filter checks if the user form the token has the necessary permissions for the requested operation.
 */
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
        Set<String> keySet = permissions.keySet();

        List<String> thePath = keySet.stream().filter(path::matches).collect(Collectors.toList());


        if (thePath.size() == 0) {
            ctx.abortWith(Response.status(Response.Status.NOT_FOUND)
                    .entity("Policy not found!")
                    .build());
            return;
        }

        if (thePath.get(0).equals("^users/.*/get") || thePath.get(0).equals("^users/changePassword") || thePath.get(0).equals("^users/permissions")) {
            String json = null;
            try {
                json = IOUtils.toString(ctx.getEntityStream(), Charsets.UTF_8);
                InputStream in = IOUtils.toInputStream(json);
                ctx.setEntityStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (thePath.get(0).equals("^users/permissions"))
            if ((thePath.get(0).equals("^users/permissions") && json != null && json.split("\"username\":\"")[1].split("\"")[0].equals(TokenManager.decodeJWT(ctx.getHeaderString("Authorization").split(" ")[1]).getSubject())))
                return;


            if(thePath.get(0).equals("^users/changePassword") || thePath.get(0).equals("^users/.*/get"))
            if ((thePath.get(0).equals("^users/changePassword") && json != null && json.split("\"username\":\"")[1].split("\"")[0].equals(TokenManager.decodeJWT(ctx.getHeaderString("Authorization").split(" ")[1]).getSubject())) ||
                    (thePath.get(0).equals("^users/.*/get") && json != null && ctx.getUriInfo().getPath().split("/")[1].equals(TokenManager.decodeJWT(ctx.getHeaderString("Authorization").split(" ")[1]).getSubject()))
            )
                return;
            else
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Permissions missing")
                        .build());
        }

        List<String> permissionsRequired = permissions.get(thePath.get(0));

        if (permissionsRequired.size() != 0) {

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
            }
            String header = rawheader.split(" ")[1];
            if (TokenManager.decodeJWT(header).getExpiration().toInstant().toEpochMilli() < Calendar.getInstance().getTime().toInstant().toEpochMilli()) {
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Auth Token Expired")
                        .build());
            }
            try {
                if (checkAccessAndSetSecurityContext(ctx, header, permissionsRequired))
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

    private boolean checkAccessAndSetSecurityContext(ContainerRequestContext requestContext, String token, List<String> permissions) throws BusinessException {
        String username = TokenManager.decodeJWT(token).getSubject();
        List<Permission> userPermissions = userService.getUserPermissionsByUsername(username);

        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> username;
            }

            @Override
            public boolean isUserInRole(String permission) {
                return userPermissions.stream().anyMatch(p -> p.getType().equals(permission));
            }

            @Override
            public boolean isSecure() {
                return true;
            }

            @Override
            public String getAuthenticationScheme() {
                return "token";
            }
        });

        return permissions.stream().anyMatch(s -> userPermissions.stream().anyMatch(ss -> ss.getType().equals(s)));

    }
}
