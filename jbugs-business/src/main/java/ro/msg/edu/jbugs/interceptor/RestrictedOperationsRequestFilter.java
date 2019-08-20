package ro.msg.edu.jbugs.interceptor;

import ro.msg.edu.jbugs.TokenManager;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.services.impl.PermissionService;
import ro.msg.edu.jbugs.services.impl.UserService;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

@Provider
public class RestrictedOperationsRequestFilter implements ContainerRequestFilter {

    @EJB
    UserService userService;

    @EJB
    PermissionService permissionService;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {

        if (ctx.getUriInfo().getPath().contains("login"))
            return;

        if (ctx.getLanguage() != null && "EN".equals(ctx.getLanguage()
                .getLanguage())) {

            ctx.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("Cannot access")
                    .build());
        }
        String header = ctx.getHeaderString("Authorization").split(" ")[1];
        System.out.println(checkAccess(ctx.getUriInfo().getPath(), header));
    }

    private boolean checkAccess(String path, String token) {
        String username = TokenManager.decodeJWT(token).getSubject();
        List<Permission> permissions = userService.getUserPermissionsByUsername(username);
//        List<UserDto> allusers = userService.getAllUser();
//        Optional<UserDto> userDto = allusers.stream().filter(s -> s.getUsername() != username).findFirst();
//        System.out.println(userDto.toString());
//        return true;
        if (path.contains("ttoken")) {
            if (permissions.stream().anyMatch(s -> s.getType().equals("USER_MANAGEMENT"))) {
                return true;
            } else
                return false;
        }
        return true;
    }
}
