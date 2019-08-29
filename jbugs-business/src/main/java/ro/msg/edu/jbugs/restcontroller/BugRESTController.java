package ro.msg.edu.jbugs.restcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.DocumentException;
import ro.msg.edu.jbugs.dto.AttachmentDto;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.entity.Attachment;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.services.impl.AttachmentService;
import ro.msg.edu.jbugs.services.impl.BugService;
import ro.msg.edu.jbugs.services.impl.UserService;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

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

    @EJB
    UserService userService;

    @EJB
    AttachmentService attachmentService;

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
    public Response getBugsAfterSearchCriteria(BugDto bugDto) {
        try {
            List<BugDto> list = bugService.getBugsAfterCriteris(bugDto);
            return Response.status(200).entity(list).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("attachments")
    public Response getBugAttachments(BugDto bugDto) {
        return Response.status(200).entity(bugService.getAttachments(bugDto)).build();
    }


    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@NotNull @FormParam("bug") BugDto bug, @NotNull @FormParam("attachment") AttachmentDto attachment) {
        Gson gson = new GsonBuilder().create();
        try {
            //clients sends username for the createdBy field and we need its id
            String usernameCreated = bug.getCreated();
            UserDto userCreated = userService.findUser(usernameCreated);
            Integer id = userCreated.getId();

            //client sends username for assignedTo field and we need its id
            String usernameAssigned = bug.getAssigned();
            UserDto userAssigned = userService.findUser(usernameAssigned);
            Integer idAssigned = userAssigned.getId();

            bug.setCreated(id.toString());
            bug.setAssigned(idAssigned.toString());

            //adds the bug
            BugDto bugAdded = bugService.addBug(bug);
            bugService.addAttachment(bugAdded, attachment);

            String response = gson.toJson("All OK!");
            return Response.status(200).entity(response).build();
        } catch (Exception e) {
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }


    @PUT
    @Path("{id}/edit")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Response editBug(@NotNull @FormParam("bug") BugDto bugDto, @NotNull @FormParam("attachment") AttachmentDto attachment) {
        try {
            if (bugService.getAttachments(bugDto).stream().noneMatch(att -> att.getAttContent().equals(attachment.getAttContent()))) {
                bugService.addAttachment(bugDto, attachment);
            }
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

    @POST
    @Path("/getPDF")
    public Response getPDF(BugDto bugDto) {
        Gson gson = new GsonBuilder().create();
        try {
            String pdf = bugService.makePDF(bugDto);
            String response = gson.toJson(pdf);
            return Response.status(200).entity(response).build();
        } catch (DocumentException e) {
            String responseError = gson.toJson(e);
            return Response.status(500).entity(responseError).build();
        } catch (IOException e) {
            String responseError = gson.toJson(e);
            return Response.status(500).entity(responseError).build();
        }
    }

    @DELETE
    @Path("/delete-attachment/{id}")
    public Response deleteAttachment(@PathParam("id") int id)
    {
        attachmentService.deleteAttachment(id);
        return Response.status(200).build();
    }

}
