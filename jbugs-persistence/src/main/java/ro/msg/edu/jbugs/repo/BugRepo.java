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
 * Repository for the bug entity. The entity
 * manager accesses the database and returns, adds, removes
 * or updates rows of the bugs table.
 * @author msg systems AG; team D.
 * @since 19.1.2
 */

@Stateless
public class BugRepo {

    //accesses the bugs table
    @PersistenceContext(unitName = "jbugs-persistence")
    EntityManager entityManager;

    /**
     * Adds a new bug in the bug table.
     *
     * @param bug - Bug; the bug that has to be inserted
     *            into the table
     * @return the bug that was inserted into the table
     */
    public Bug addBug(Bug bug){
        entityManager.persist(bug);
        entityManager.flush();
        return bug;
    }

    /**
     * Finds a bug in the bug table after its Id.
     * @param id - Integer; the Id for the search criteria
     * @return the bug from the table with the given id
     * */
    public Bug findBug(Integer id) throws EntityNotFoundException {
        return entityManager.find(Bug.class,id);
    }

    /**
     * Returns all the bugs from the table.
     * @return list of the entire bugs from the database
     * */
    public List<Bug> getAllBugs(){
        Query query  = entityManager.createNamedQuery(Bug.GET_ALL_BUGS,Bug.class);
        List<Bug> bugs = query.getResultList();
        return bugs;
    }

    /**
     * Deletes the bugs that are older than the given date
     * @param date - date; the date for the delete criteria
     * @return 1 if any bug was deleted
     *         0 if no bug was deleted
     * */
    public Integer deleteOldBugs(Date date){
        Query query = entityManager.createNamedQuery(Bug.DELETE_OLD_BUGS);
        query.setParameter("date",date);
        Integer result = query.executeUpdate();
        return result;
    }

    /**
     * Deletes a bug after a given id of the user.
     * @param user - User; the user whose bug belongs to
     * @return 1 if any bug was deleted
     *         0 if no bug was deleted
     * */
    public Integer deleteBugAfterUserId(User user){
        Query query = entityManager.createNamedQuery(Bug.DELETE_BUG_AFTER_USER_ID);
        query.setParameter("user",user);
        Integer result = query.executeUpdate();
        return result;
    }

    /**
     * Returns the bugs that belong to a given user.
     * @param  user - User; the user whose bugs belong to
     * @return list of all found bugs
     * */
    public List<Bug> findBugAfterUserId(User user) {
        Query query = entityManager.createNamedQuery(Bug.FIND_BUG_AFTER_USER_ID);
        query.setParameter("user",user);
        List<Bug> bugs = query.getResultList();
        return bugs;
    }

    /**
     * Returns all the bugs with the given severity and status criteria.
     * @param severity - Severity; the severity for the search criteria
     * @param status - Status; the staturs for the search criteria
     * @param creator - String; the username of the User that created the bug
     * @param assigned - String; the username of the User to whom the bug
     *                 was assigned
     * @return list of all bugs found with the given criterias
     * */
    //Todo criteri de cautare si posibil paginarea
    public List<Bug> getBugsAfterSearchCriteriaWithSeverityAndStatus(Bug.Severity severity, Bug.Status status,
                                                                     String creator, String assigned) {
        Query query = entityManager.createNamedQuery(Bug.SEARCH_CRITERIA_WITH_STATUS_AND_SEVERITY);
        query.setParameter("severity", severity);
        query.setParameter("status", status);
        query.setParameter("creator", creator);
        query.setParameter("assigned", assigned);
        return query.getResultList();
    }

    /**
     * Returns all the bugs with the given creator and assigned user criteria.
     * @param creator - String; the username of the user that created the bug
     * @param assigned - String; the username of the user tho whom the bug was
     *                 assigned
     * @return list of all bugs found with the given criterias
     * */
    public List<Bug> getBugsAfterSearchCriteriaUsers(String creator, String assigned) {
        Query query = entityManager.createNamedQuery(Bug.SEARCH_CRITERIA_WITHOUT_STATUS_AND_SEVERITY);
        query.setParameter("creator", creator);
        query.setParameter("assigned", assigned);
        return query.getResultList();
    }


    /**
     * Returns all the bugs with the given severity, creator and assigned user criteria.
     * @param severity - Severity; the severity for the search criteria
     * @param creator - String; the username of the user that created the bug
     * @param assigned - String; the username of the user tho whom the bug was
     *                 assigned
     * @return list of all bugs found with the given criterias
     * */
    public List<Bug> getBugsAfterSearchCriteriaWithSeverity(Bug.Severity severity, String creator, String assigned) {
        Query query = entityManager.createNamedQuery(Bug.SEARCH_CRITERIA_WITH_SEVERITY);
        query.setParameter("severity", severity);
        query.setParameter("creator", creator);
        query.setParameter("assigned", assigned);
        return query.getResultList();
    }

    /**
     * Returns all the bugs with the given status, creator and assigned user criteria.
     * @param status - Status; the status for the search criteria
     * @param creator - String; the username of the user that created the bug
     * @param assigned - String; the username of the user tho whom the bug was
     *                 assigned
     * @return list of all bugs found with the given criterias
     * */
    public List<Bug> getBugsAfterSearchCriteriaWithStatus(Bug.Status status, String creator, String assigned) {
        Query query = entityManager.createNamedQuery(Bug.SEARCH_CRITERIA_WITH_STATUS);
        query.setParameter("status", status);
        query.setParameter("creator", creator);
        query.setParameter("assigned", assigned);
        return query.getResultList();
    }

    /**
     * Updates a bug from the table.
     * @param bug - the bug that has to be updated
     * @return the updated bug
     * */
    public Bug update(Bug bug) {
        return entityManager.merge(bug);
    }
}
