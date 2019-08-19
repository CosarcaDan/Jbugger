package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BuisnissException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */

@Stateless
public class UserRepo {
    @PersistenceContext(unitName = "jbugs-persistence")
    private EntityManager entityManager;

    @EJB
    private AttachmentRepo attachmentRepo;

    @EJB
    private BugRepo bugRepo;

    @EJB
    private CommentRepo commentRepo;

    @EJB
    private NotificationRepo notificationRepo;


    public User addUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }


    //toDo sterge sau trateaza posibila exceptie
    public User findUser(Integer id) throws EntityNotFoundException {
        return entityManager.getReference(User.class, id);
    }


    public User findeUserAfterUsername(String username) throws BuisnissException {
        Query query = entityManager.createNamedQuery(User.QUERY_SELECT_AFTER_USERNAME);
        query.setParameter("username", username);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BuisnissException("no user wit given username", "msg-003");
        }
    }

    public List<User> getAllUser() {
        Query query = entityManager.createNamedQuery(User.QUERY_SELECT_ALL_USER, User.class);
        List<User> userList = query.getResultList();
        return userList;
    }


    //toDo pastreaza ca si un delete permanent sau sterge
    public Boolean getAllDToBeDeleted() throws Exception {

        Query query = entityManager.createNamedQuery(User.QUERY_SELECT_AFTER_USERNAME);
        query.setParameter("username", "toBeDeleted");
        //modifica la getSIngleResult
        //User user1 = (User)query.getSingleResult();
        int user1 = query.getFirstResult();
        query.setParameter("username", "toBeDeleted2");
        //modifica la getSIngleResult
        //User user2 = (User)query.getSingleResult();
        int user2 = query.getFirstResult();
        return user1 != 1 && user2 != 1;
    }


    //ToDo dezactivarea de user
    //ToDo refactor functia de stergere
    public Integer deleteUserAfterUserName(String username) throws BuisnissException {
        Integer result = -1;
        User user = findeUserAfterUsername(username);
        if (user != null) {
            List<Bug> bugList = bugRepo.findBugAfterUserId(user);
            for (Bug bug : bugList) {
                attachmentRepo.deleteAttachmentAfterBugId(bug);
            }
            commentRepo.deleteCommentAfterUserId(user);
            notificationRepo.deleteNotificationAfterUserId(user);
            bugRepo.deleteBugAfterUserId(user);
            Query query = entityManager.createNamedQuery(User.QUERY_REMOVE_AFTER_USERNAME);
            query.setParameter("username", username);
            result = query.executeUpdate();
        }
        return result;
    }


    //ToDo Posibil sa facem username in bd unique si sa nu mai folosim functia?
    public boolean isUsernameUnique(String username) {
        Query query = entityManager.createNamedQuery(User.QUERY_COUNT_USER_NAME_UNIQUE);
        query.setParameter("username", username);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;

        }
    }

    public User login(String username, String password) throws BuisnissException {
        Query query = entityManager.createNamedQuery(User.QUERY_USER_LOGIN_AFTER_USERNAME_PASSWORD);
        query.setParameter("username", username);
        query.setParameter("password", password);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BuisnissException("login Failed", "msg-001");
        }
    }

    public User resetLoginFailCounter(User user) throws BuisnissException {
        user.setCounter(0);
        return user;
    }

    //ToDo reactivate User


    //toDo move to service
    public void passwordFailed(String username) throws BuisnissException {
        User user = findeUserAfterUsername(username);
        if (user.getCounter() < 5) {
            user.setCounter(user.getCounter() + 1);
            if (user.getCounter() == 5) {
                user.setStatus(false);
                throw new BuisnissException("Password failed to may times, User deactivated", "msg - 003");
            }
        }else {
            throw new BuisnissException("User Inactive", "msg - 005");
        }
    }

    //ToDO sterge add si delete defaults
    public Boolean deleteDefault() throws Exception {

        Integer result1 = deleteUserAfterUserName("toBeDeleted");
        Integer result2 = deleteUserAfterUserName("toBeDeleted2");
        return result1 == 1 && result2 == 1;
    }

    ///////////////

    public List<Bug> getAllCreatedBugs(User user) {
        return user.getCreatedBy();
    }

    public List<Bug> getAllAssignedBugs(User user) {
        return user.getAssignedTo();
    }


}
