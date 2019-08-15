package ro.msg.edu.jbugs.timer;

import ro.msg.edu.jbugs.services.impl.BugService;
import ro.msg.edu.jbugs.services.impl.CommentService;

import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Singleton;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Singleton
public class WorkerBean {

    @EJB
    private BugService bugService;

    @EJB
    private CommentService commentService;

    @AccessTimeout(0)
    public void doTimerWork() throws Exception {
        //Integer commentsDeleted = commentService.deleteOldComment();
        //Integer bugsDeleted = bugService.deleteOldBugs();
        //EmailSender.sendMail("Comments: "+commentsDeleted.toString() + " - Bugs: " + bugsDeleted.toString() );
    }
}
