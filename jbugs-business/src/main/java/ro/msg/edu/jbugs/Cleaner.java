package ro.msg.edu.jbugs;

import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.repo.BugRepo;
import ro.msg.edu.jbugs.repo.CommentRepo;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;


@Stateless
@Interceptors(LoggingInterceptor.class)
public class Cleaner {
    @EJB
    private CommentRepo commentRepo;

    @Schedule(minute = "*/3", hour = "*")
    public void clean() {
        Integer commentres = commentRepo.removeOld();
//        String message = "Bugs closed: "+ bugres+" \nComments deleted: "+commentres+". ";
//        Email.sendMail("Report", message);
    }
}
