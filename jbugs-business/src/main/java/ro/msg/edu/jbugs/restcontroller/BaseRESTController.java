package ro.msg.edu.jbugs.restcontroller;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/services")
public class BaseRESTController extends ResourceConfig {
    public BaseRESTController() {
        packages("ro.msg.edu.jbugs.restcontroller");
        register(MultiPartFeature.class);
    }
}
