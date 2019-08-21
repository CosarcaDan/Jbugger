package ro.msg.edu.jbugs.restcontroller;

import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.services.impl.BugService;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */

@Path("/bugs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED})
@Interceptors(LoggingInterceptor.class)
public class BugRESTController {
    @EJB
    BugService bugService;

    @GET
    public Response getAll(){
        return Response.status(200).entity( bugService.getAllBug()).build();
    }
}
