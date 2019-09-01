package ro.msg.edu.jbugs;

import ro.msg.edu.jbugs.interceptors.LoggingInterceptor;
import ro.msg.edu.jbugs.repo.CommentRepo;
import ro.msg.edu.jbugs.repo.NotificationRepo;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;


@Stateless
@Interceptors(LoggingInterceptor.class)
public class Cleaner {
    @EJB
    private CommentRepo commentRepo;
    @EJB
    private NotificationRepo notificationRepo;

    @Schedule(minute = "*/3", hour = "*")
    public void clean() {
        Integer notifications = notificationRepo.removeOld(60000*3);
    }
}
