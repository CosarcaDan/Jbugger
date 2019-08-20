package ro.msg.edu.jbugs.interceptor;

import ro.msg.edu.jbugs.TokenManager;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.services.impl.UserService;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.List;

@Provider
public class RestrictedOperationsRequestFilter implements ContainerRequestFilter {

    @EJB
    UserService userService;

    @EJB
    PathPolicy pathPolicy;
    @Override
    public void filter(ContainerRequestContext ctx){

        HashMap<String, List<String>> permissions = pathPolicy.getPathPermissions();
        String path= ctx.getUriInfo().getPath();
        List<String> permissionsRequired = permissions.get(path);

        if(permissionsRequired.size() == 0)
            return;
        else
        {

            String rawheader = ctx.getHeaderString("Authorization");
            if(rawheader == null || rawheader.equals(""))
            {
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authorization header missing!")
                        .build());
                return;
            }
            String header=rawheader.split(" ")[1];
            if(checkAccess(header,permissionsRequired))
                return;
            else
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Permissions missing")
                        .build());

        }


    }

    private boolean checkAccess(String token, List<String> permissions)
    {
        String username=TokenManager.decodeJWT(token).getSubject();
        List<Permission> userPermissions = userService.getUserPermissionsByUsername(username);
        return permissions.stream().anyMatch(s->{
            if(userPermissions.stream().anyMatch(ss->ss.getType().equals(s)))
            {
                return true;
            }
            else
                return false;
        });

    }
}
