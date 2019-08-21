package ro.msg.edu.jbugs.restcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.msg.edu.jbugs.dto.PermissionDto;
import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.services.impl.PermissionService;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED})
@Interceptors(LoggingInterceptor.class)
public class PermissionRESTController {
    @EJB
    private PermissionService permissionService;


    @GET
    public List<PermissionDto> getAll(){
        return permissionService.getAllPermissions();
    }

    @POST
    @Path("/not-in-role")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPermissionsByRole(RoleDto role) {
        Gson gson = new GsonBuilder().create();
        try {
            List<PermissionDto> result = permissionService.getPermissionsNotInRole(role);
            String response = gson.toJson(result);
            return Response.status(200).entity(response).build();
        } catch (Exception e) {

            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }

}
