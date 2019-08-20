package ro.msg.edu.jbugs.interceptors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class LoggingInterceptor {

    private static final Logger logger = LogManager.getLogger(LoggingInterceptor.class.getName());

    @AroundInvoke
    private Object doLog(InvocationContext ic) throws Exception {
        Object obj = null;
        try{
            logger.info("Entering method "+ic.getTarget().toString()+" "+ic.getMethod().getName()+".");
            obj= ic.proceed();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw e;
        }
        finally {
            logger.info("Exiting method "+ic.getTarget().toString()+" "+ic.getMethod().getName()+".");
        }
        return obj;
    }
}
