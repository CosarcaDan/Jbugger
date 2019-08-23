package ro.msg.edu.jbugs.restcontroller;

import java.io.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.pdfbox.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
@Path("/files")
public class FileRESTController {

    private String fileprefix = "files/";
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
        String fileLocation = "files/" + fileDetail.getFileName();
        try {
            int read = 0;
            byte[] bytes = new byte[1024];
            FileOutputStream  out = new FileOutputStream(new File(fileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {e.printStackTrace();}
        String output = "File successfully uploaded to : " + fileLocation;
        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/download/{filename}")
    public Response getFile(@PathParam("filename") String filename) {
        final File file = new File(fileprefix+filename);
        System.out.println(fileprefix+filename);

        StreamingOutput stream = output -> output.write(IOUtils.toByteArray(new FileInputStream(file)));

        return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "inline; filename=\"" + file.getName() + "\"")
                .build();
    }
}