package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */

@Stateless
public class BugRepo {

    @PersistenceContext(unitName = "jbugs-persistence")
    EntityManager entityManager;

    public Bug addBug(Bug bug){
        entityManager.persist(bug);
        entityManager.flush();
        return bug;
    }

    public Bug findBug(Integer id) throws EntityNotFoundException {
        return entityManager.find(Bug.class,id);
    }

    public List<Bug> getAllBugs(){
        Query query  = entityManager.createNamedQuery(Bug.GET_ALL_BUGS,Bug.class);
        List<Bug> bugList = query.getResultList();
        return bugList;
    }

    public Integer deleteOldBugs(Date date){
        Query query = entityManager.createNamedQuery(Bug.DELETE_OLD_BUGS);
        query.setParameter("date",date);
        Integer result = query.executeUpdate();
        return result;
    }

    public Integer deleteBugAfterUserId(User user){
        Query query = entityManager.createNamedQuery(Bug.DELETE_BUG_AFTER_USER_ID);
        query.setParameter("user",user);
        Integer result = query.executeUpdate();
        return result;
    }

    public List<Bug> findBugAfterUserId(User user){
        Query query = entityManager.createNamedQuery(Bug.FINDE_BUG_AFTER_USER_ID);
        query.setParameter("user",user);
        List<Bug> bugList = query.getResultList();
        return bugList;
    }

    //Todo criteri de cautare si posibil paginarea


    public List<Bug> getBugsAfterSearchCriteriaWithSeverityAndStatus(Bug.Severity severity, Bug.Status status, String creator, String assigned) {
        Query query = entityManager.createNamedQuery(Bug.SEARCH_CRITERIA_WITH_STATUS_AND_SEVERITY);
        query.setParameter("severity",severity);
        query.setParameter("status",status);
        query.setParameter("creator",creator);
        query.setParameter("assigned",assigned);
        return query.getResultList();
    }


    public List<Bug> getBugsAfterSearchCriteriaUsers(String creator, String assigned) {
        Query query = entityManager.createNamedQuery(Bug.SEARCH_CRITERIA_WITHOUT_STATUS_AND_SEVERITY);
        query.setParameter("creator",creator);
        query.setParameter("assigned",assigned);
        return query.getResultList();
    }


    public List<Bug> getBugsAfterSearchCriteriaWithSeverity(Bug.Severity severity, String creator, String assigned) {
        Query query = entityManager.createNamedQuery(Bug.SEARCH_CRITERIA_WITH_STATUS_AND_SEVERITY);
        query.setParameter("severity",severity);
        query.setParameter("creator",creator);
        query.setParameter("assigned",assigned);
        return query.getResultList();
    }

    public List<Bug> getBugsAfterSearchCriteriaWithStatus(Bug.Status status, String creator, String assigned) {
        Query query = entityManager.createNamedQuery(Bug.SEARCH_CRITERIA_WITH_STATUS_AND_SEVERITY);
        query.setParameter("status",status);
        query.setParameter("creator",creator);
        query.setParameter("assigned",assigned);
        return query.getResultList();
    }


    public User getCreator(Bug bug){
        return bug.getCreated();
    }

    public User getAssigned(Bug bug){
        return bug.getAssigned();
    }
}
