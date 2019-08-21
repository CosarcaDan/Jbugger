package ro.msg.edu.jbugs.restcontroller;

import ro.msg.edu.jbugs.dto.RoleDto;
import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.services.impl.RoleService;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED})
@Interceptors(LoggingInterceptor.class)
public class RoleRESTController {

    @EJB
    private RoleService roleService;

    @GET
    public List<RoleDto> getAll(){
        return roleService.getAllRoles();
    }
}
