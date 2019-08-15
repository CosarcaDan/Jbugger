package ro.msg.edu.jbugs.timer;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Singleton
public class TimerBean {

    @EJB
    private WorkerBean workerBean;

    @Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void atSchedule() {

        try {
            //workerBean.doTimerWork();
        } catch (Exception e) {
            System.out.println("timer still busy");
        }
    }

}
