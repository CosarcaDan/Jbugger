package ro.msg.edu.jbugs.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class CallDurationInterceptor {

    @AroundInvoke
    private Object calculateDuration(InvocationContext context){
        Object obj = null;

        try{
            PrintWriter writer = new PrintWriter("C:\\Training\\20_TOOLS\\servers\\payara41\\glassfish\\domains\\domain1\\logs\\callduration.txt", "UTF-8");
            long start = System.currentTimeMillis();
            obj = context.proceed();
            long end = System.currentTimeMillis();
            Logger log =  Logger.getLogger(CallDurationInterceptor.class.getName());
            log.info("Took :-------------- " + (end - start));
            writer.println("Took :" + (end - start));
            writer.close();
        } catch (Exception e) {
            System.out.println("Took : -1" );
        }
        return obj;
    }
}
