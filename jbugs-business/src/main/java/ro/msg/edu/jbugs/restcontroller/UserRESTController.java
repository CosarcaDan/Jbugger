package ro.msg.edu.jbugs.restcontroller;

import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.services.impl.UserService;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Interceptors(LoggingInterceptor.class)
public class UserRESTController {
    @EJB
    private UserService userService;


    @GET
    public List<UserDto> getAll(){
        return userService.getAllUser();
    }
}