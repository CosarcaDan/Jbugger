package ro.msg.edu.jbugs.restcontroller;

import org.apache.pdfbox.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;

/**
 * Rest controller for the entity Attachment.
 *
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
@Path("/files")
public class FileRESTController {

    private static final String filePrefix = "files/";

    /**
     * POST request that uploads a file sent by the client
     * in the directory C:/files with the given filename.
     * URL : /files/upload
     * */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
        String fileLocation = "files/" + fileDetail.getFileName();
        try {
            int read;
            byte[] bytes = new byte[1024];
            FileOutputStream out = new FileOutputStream(new File(fileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(200).build();
    }

    /**
     * GET request that downloads the required file to the client
     * in the directory C:/files with the given filename.
     * URL : /files/download/{filename}
     * */
    @GET
    @Path("/download/{filename}")
    public Response getFile(@PathParam("filename") String filename) {
        final File file = new File(filePrefix + filename);
        StreamingOutput stream = output -> output.write(IOUtils.toByteArray(new FileInputStream(file)));
        return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "inline; filename=\"" + file.getName() + "\"")
                .build();
    }
}
