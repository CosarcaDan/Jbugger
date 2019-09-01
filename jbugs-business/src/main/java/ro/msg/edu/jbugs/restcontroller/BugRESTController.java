package ro.msg.edu.jbugs.restcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.DocumentException;
import org.glassfish.jersey.media.multipart.FormDataParam;
import ro.msg.edu.jbugs.dto.AttachmentDto;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
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
 * Rest controller for the entity Bug.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
@Path("/bugs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Interceptors(LoggingInterceptor.class)
public class BugRESTController {

    //injects the service of the Bug entity
    @EJB
    BugService bugService;

    //injects the service of the User entity
    @EJB
    UserService userService;

    //injects the service of the Attachment entity
    @EJB
    AttachmentService attachmentService;

    /**
     * GET request that returns all the bugs from the
     * database.
     * URL : /bugs
     */
    @GET
    public Response getAll() {
        return Response.status(200).entity(bugService.getAllBugs()).build();
    }

    /**
     * GET request that returns the bug with a given Id in
     * case this exists. Otherwise, it returns an internal
     * server error: the bug with the given Id does not exist.
     *
     * @param id - int; ID of the bug that has to be returned
     *           URL: /bugs/{id}
     */
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


    /**
     * DELETE request that deletes the bug with a given Id in
     * case this exists. Otherwise, it returns an internal
     * server error: the bug with the given Id does not exist
     * and can not be deleted.
     * @param id - int; the id of the bug that has to be
     *           closed
     * URL: /bugs/{id}
     * */
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

    /**
     * POST request that returns the bugs with the requested search
     * criteria in case these exist. Otherwise, it returns an internal
     * server error: no bugs were found.
     * @param bugDto - BugDto; the bug with the search criteria
     * URL: /bugs
     * */
    @POST
    public Response getBugsAfterSearchCriteria(BugDto bugDto) {
        try {
            List<BugDto> list = bugService.getBugsAfterCriteria(bugDto);
            return Response.status(200).entity(list).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    /**
     * POST request that returns all the found attachments of a given bug
     * in case these exist.
     * @param bugDto - BugDto; the bug whose attachments are requested
     * URL: /bugs/attachments
     * */
    @POST
    @Path("attachments")
    public Response getBugAttachments(BugDto bugDto) {
        return Response.status(200).entity(bugService.getAttachments(bugDto)).build();
    }


    /**
     * POST request that inserts a bug into the database in case
     * there are no errors for the created/ assigned user.
     * Otherwise, it returns an internal server error: bug was
     * not inserted, because its validations failed.
     * @param bug - BugDto; the bug that needs to be added in the
     *            database
     * @param  attachment - AttachmentDto; the attachment of
     *                    the bug that has to be added
     * URL: /bugs/add
     * */
    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@NotNull @FormParam("bug") BugDto bug,
                        @NotNull @FormParam("attachment") AttachmentDto attachment) {
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
            if (attachment.getAttContent() != null) {
                bugService.addAttachment(bugAdded, attachment);
            }
            String response = gson.toJson("All OK!");
            return Response.status(200).entity(response).build();
        } catch (Exception e) {
            String error = gson.toJson(e);
            return Response.status(500).entity(error).build();
        }
    }

    /**
     * PUT request that edits a bug from the database in case
     * the bug exists.
     * Otherwise, it returns an internal server error: bug was
     * not found and it could't be edited.
     * @param bugDto - BugDto; the bugDto that has to be edited
     * @param attachment - AttachmentDto; the attachment that has
     *                   to be added for the bug
     * URL: /bugs/{id}/edit
     * */
    @PUT
    @Path("{id}/edit")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Response editBug(@NotNull @FormParam("bug") BugDto bugDto,
                            @NotNull @FormParam("attachment") AttachmentDto attachment) {
        try {
            if (bugService.getAttachments(bugDto).stream()
                    .noneMatch(att -> att.getAttContent().equals(attachment.getAttContent()))) {
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

    /**
     * PUT request that edits the status of a bug from the database
     * in case the bug exists and the status is valid.
     * Otherwise, it returns an internal server error: bug status was
     * not updated.
     * @param bugDto - BugDto; the bug whose status must be changed
     * URL: /bugs/{id}/status
     * */
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

    /**
     * POST request that generates a new PDF file for the given
     * bug if it is valid and found in the database.
     * Otherwise, it returns an internal server error: PDF file
     * was not generated.
     * @param bugDto - BugDto; the bug for which the PDF must be
     *               generated
     * URL: /bugs/getPDF
     * */
    @POST
    @Path("/getPDF")
    public Response getPDF(BugDto bugDto) {
        Gson gson = new GsonBuilder().create();
        try {
            String pdf = bugService.makePDF(bugDto);
            String response = gson.toJson(pdf);
            return Response.status(200).entity(response).build();
        } catch (DocumentException | IOException e) {
            String responseError = gson.toJson(e);
            return Response.status(500).entity(responseError).build();
        }
    }

    /**
     * POST request that deletes the required attachment
     * of a given bug if the attachment and the bug exist in the
     * database.
     * @param bug - BugDto; the bug whose attachment must be deleted
     * @param id - int; the Id of the attachment that needs to be
     *           deleted
     * URL: /bugs/delete-attachment
     * */
    @POST
    @Path("/delete-attachment")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deleteAttachment(@FormDataParam("bug") BugDto bug, @FormDataParam("id") Integer id) {
        this.bugService.deleteAttachment(bug, id);
        attachmentService.deleteAttachment(id);
        return Response.status(200).build();
    }
}
