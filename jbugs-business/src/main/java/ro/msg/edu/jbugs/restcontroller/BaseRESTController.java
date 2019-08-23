package ro.msg.edu.jbugs.restcontroller;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import ro.msg.edu.jbugs.interceptor.RestrictedOperationsRequestFilter;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/services")
public class BaseRESTController extends ResourceConfig {
    public BaseRESTController() {
        packages("ro.msg.edu.jbugs.restcontroller");
        register(MultiPartFeature.class);
        register(JacksonJsonProvider.class);
        register(CorsFilter.class);
        register(RestrictedOperationsRequestFilter.class);
    }
}
