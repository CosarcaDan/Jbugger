package ro.msg.edu.jbugs.restcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import ro.msg.edu.jbugs.MyToken;
import ro.msg.edu.jbugs.TokenManager;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.services.impl.UserService;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED})
@Interceptors(LoggingInterceptor.class)
public class UserRESTController {
    @EJB
    private UserService userService;


    @GET
    public List<UserDto> getAll(){
        return userService.getAllUser();
    }
    @POST
    @Path("/login")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public void login(@FormParam("username") String username,
//                      @FormParam("password") String password){
//        UserLoginDto userLoginDto = new UserLoginDto(username,password);
//        UserDto user= new UserDto(userLoginDto);
//        try
//        {
//            UserDto loged_in = userService.login(user);
//
//        }
//        catch (BusinessException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(user.toString());
//    }
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(UserDto user) {
        Gson gson = new GsonBuilder().create();
        try {
            UserDto loged_in = userService.login(user);
            MyToken myToken = new MyToken(TokenManager.createJWT(loged_in.getId().toString(), "server", loged_in.getUsername(), 123456789));
            String response = gson.toJson(myToken);
            return Response.status(200).entity(response).build();
        } catch (BusinessException e) {

            String error = gson.toJson(e);
            //mapper.writeValueAsString(userList);
            return Response.status(500).entity(error).build();
        }
    }


    @POST
    @Path("/ttoken")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String testToken(String token){
        Claims claims = TokenManager.decodeJWT(token);
        String subject = claims.get("sub").toString();
        return "good token " + subject;
    }


}