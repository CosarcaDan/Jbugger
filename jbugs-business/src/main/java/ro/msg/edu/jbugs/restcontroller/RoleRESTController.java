package ro.msg.edu.jbugs.restcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.msg.edu.jbugs.dto.PermissionDto;
import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.services.impl.RoleService;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED})
@Interceptors(LoggingInterceptor.class)
public class RoleRESTController {
    @EJB
    private RoleService roleService;

//    @OPTIONS
//    public Response getOptions() {
//        return Response.ok()
//                .header("Access-Control-Allow-Origin", "*")
//                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
//                .header("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization").build();
//    }

    @GET
    public List<RoleDto> getAll(){
        return roleService.getAllRoles();
    }

    @POST
    @Path("/permissions")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPermissionsByRole(RoleDto role) {
        Gson gson = new GsonBuilder().create();
        try {
            List<PermissionDto> result = roleService.getPermissionsByRole(role);
            String response = gson.toJson(result);
            return Response.status(200).entity(response).build();
        } catch (Exception e) {

            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }
    @POST
    @Path("/add-permissions")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPermissionsToRole(@NotNull @FormParam("role")RoleDto role,@NotNull @FormParam("permissions") String permissions) {
        Gson gson = new GsonBuilder().create();
        try {
            PermissionDto[] list = gson.fromJson(permissions, PermissionDto[].class);
            Arrays.stream(list).forEach(permission -> roleService.addPermissionToRole(role,permission));
            String response = gson.toJson("All OK!");
            return Response.status(200).entity(response).build();
        } catch (Exception e) {

            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }
    @POST
    @Path("/remove-permissions")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public Response removePermissionsToRole(@NotNull @FormParam("role")RoleDto role,@NotNull @FormParam("permissions") String permissions) {
        Gson gson = new GsonBuilder().create();
        try {
            PermissionDto[] list = gson.fromJson(permissions, PermissionDto[].class);
            Arrays.stream(list).forEach(permission -> roleService.removePermissionToRole(role,permission));
            String response = gson.toJson("All OK!");
            return Response.status(200).entity(response).build();
        } catch (Exception e) {

            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }

}
