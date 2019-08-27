package ro.msg.edu.jbugs.restcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.media.multipart.FormDataParam;
import ro.msg.edu.jbugs.MyToken;
import ro.msg.edu.jbugs.TokenManager;
import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.dto.mappers.PermissionDtoMapping;
import ro.msg.edu.jbugs.dto.mappers.UserDtoMapping;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.services.impl.UserService;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED})
@Interceptors(LoggingInterceptor.class)
public class
UserRESTController {
    @EJB
    private UserService userService;


    @GET
    public List<UserDto> getAll(){
        return userService.getAllUser();
    }

    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserDto user) {
        Gson gson = new GsonBuilder().create();
        try {
            UserDto loged_in = userService.login(user);
            MyToken myToken = new MyToken(TokenManager.createJWT(loged_in.getId().toString(), "server", loged_in.getUsername(), 123456789));
            String response = gson.toJson(myToken);
            userService.deactivateUser(loged_in.getUsername(), true);
            return Response.status(200).entity(response).build();
        } catch (BusinessException e) {
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }

    @POST
    @Path("/renew")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response renewToken(UserDto user) {
        Gson gson = new GsonBuilder().create();
        try {
            user = userService.findUser(user.getUsername());
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        MyToken myToken = new MyToken(TokenManager.createJWT(user.getId().toString(), "server", user.getUsername(), 123456789));
        String response = gson.toJson(myToken);
        return Response.status(200).entity(response).build();
    }

    @POST
    @Path("/permissions")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response UserPermissions(UserDto user) {
        Gson gson = new GsonBuilder().create();
        try {
            List<Permission> userPerm = userService.getUserPermissionsByUsername(user.getUsername());
            StringBuilder response = new StringBuilder("[");
            userPerm.forEach(p -> {
                response.append(gson.toJson(PermissionDtoMapping.permissionToPermissionDto(p)));
                response.append(",");
            });
            response.deleteCharAt(response.length() - 1);
            response.append("]");
            return Response.status(200).entity(response.toString()).build();
        } catch (BusinessException e) {
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response testToken(UserDto user) {
        Gson gson = new GsonBuilder().create();
        try {
            userService.activateUser(user.getUsername());
            return Response.status(200).entity(gson.toJson("OK!")).build();
        } catch (BusinessException e) {
            e.printStackTrace();
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }


    @POST
    @Path("/add")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@NotNull @FormDataParam("user") UserDto user, @NotNull @FormDataParam("roles") String roles) {
        Gson gson = new GsonBuilder().create();
        try {
            //adds the user
            User userAdded = userService.addUser(user);
            UserDto userAddededDto = UserDtoMapping.userToUserDtoIncomplet(userAdded);
            //adds the roles of the user
            RoleDto[] list = gson.fromJson(roles, RoleDto[].class);
            Arrays.stream(list).forEach(role -> userService.addRoleToUser(userAddededDto, role));
            String response = gson.toJson("User was successfully added!");
            return Response.status(200).entity(response).build();
        } catch (Exception e) {
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") int id) {
        Gson gson = new GsonBuilder().create();
        try {
            UserDto userDto = userService.findUser(id);
            String response = gson.toJson(userDto);
            return Response.status(200).entity(response).build();
        } catch (BusinessException e) {
            String responseError = gson.toJson(e);
            return Response.status(500).entity(responseError).build();
        }
    }

    @PUT
    @Path("{id}/edit")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response edit(@NotNull @FormDataParam("user") UserDto userDto, @NotNull @FormDataParam("roles") String roles) {
        Gson gson = new GsonBuilder().create();
        try {
            this.userService.updateWithRoles(userDto, Arrays.asList((gson.fromJson(roles, RoleDto[].class))));
            String response = gson.toJson("User was successfully edited!");
            return Response.status(200).entity(response).build();
        } catch (Exception e) {
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }

    @PUT
    @Path("/{id}/activate")
    public Response activate(@NotNull UserDto user) {
        Gson gson = new GsonBuilder().create();
        try {
            //user.setStatus(true);
            userService.updateUser(user);
            String response = gson.toJson("User was successfully activated!");
            return Response.status(200).entity(response).build();
        } catch (Exception e) {
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }

    @PUT
    @Path("/{id}/deactivate")
    public Response deactivate(@NotNull UserDto user) {
        Gson gson = new GsonBuilder().create();
        String response;
        try {
            //user.setStatus(false);
            if (userService.hasOnlyClosedBugs(user)) {
                userService.updateUser(user);
                response = gson.toJson("User was successfully deactivated!");
            } else {
                response = gson.toJson("User has tasks assigned, that are not closed yet and" +
                        " cannot be deleted!");
            }
            return Response.status(200).entity(response).build();
        } catch (Exception e) {
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }

    @POST
    @Path("/roles")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRoles(Integer id) {
        Gson gson = new GsonBuilder().create();
        try {
            List<RoleDto> roles = userService.getAllRoles(id);
            String response = gson.toJson(roles);
            return Response.status(200).entity(response).build();
        } catch (BusinessException e) {
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }



}
