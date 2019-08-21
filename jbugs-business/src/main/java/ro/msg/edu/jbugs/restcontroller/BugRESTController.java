package ro.msg.edu.jbugs.restcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.services.impl.BugService;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */

@Path("/bugs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Interceptors(LoggingInterceptor.class)
public class BugRESTController {
    @EJB
    BugService bugService;

    @GET
    public Response getAll() {
        return Response.status(200).entity(bugService.getAllBug()).build();
    }

    @GET
    @Path("{id}")
    public Response getBug(@PathParam("id") int id) {
        Gson gson = new GsonBuilder().create();
        try {
            BugDto bugDto = bugService.findBug(id);
            String response = gson.toJson(bugDto);
            return Response.status(200).entity(response).build();

        } catch (BusinessException e) {
            String responseError = gson.toJson(e);
            return Response.status(500).entity(responseError).build();
        }
    }


    @DELETE
    @Path("{id}")
    public Response deleteOrderById(@PathParam("id") int id) {
        try {
            BugDto bugDto = bugService.findBug(id);
            bugService.closeBug(bugDto);
            return Response.status(200).build();

        } catch (BusinessException e) {
            Gson gson = new GsonBuilder().create();
            String responseError = gson.toJson(e);
            return Response.status(500).entity(responseError).build();
        }
    }

    @POST
    @Path("addBug")
    public Response addBug(BugDto bugDto) {
        try {
            bugService.addBug(bugDto);
            return Response.status(200).build();
        } catch (BusinessException e) {
            Gson gson = new GsonBuilder().create();
            String responseError = gson.toJson(e);
            return Response.status(500).entity(responseError).build();
        }
    }


    @PUT
    @Path("{id}/edit")
    public Response editBug(BugDto bugDto) {
        try {
            bugService.updateBug(bugDto);
            return Response.status(200).build();
        } catch (BusinessException e) {
            Gson gson = new GsonBuilder().create();
            String responseError = gson.toJson(e);
            return Response.status(500).entity(responseError).build();
        }
    }

    @PUT
    @Path("{id}/status")
    public Response changeStatus(BugDto bugDto) {
        try {
            bugService.updateStatusBug(bugDto);
            return Response.status(200).build();
        } catch (BusinessException e) {
            Gson gson = new GsonBuilder().create();
            String responseError = gson.toJson(e);
            return Response.status(500).entity(responseError).build();
        }
    }








}
