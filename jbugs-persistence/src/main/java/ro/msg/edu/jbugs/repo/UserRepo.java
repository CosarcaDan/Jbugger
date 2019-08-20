package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.entity.Role;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BusinessException;

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


    public User findUser(Integer id) throws EntityNotFoundException {
        return entityManager.getReference(User.class, id);
    }

    public User findeUserAfterUsername(String username) throws BusinessException {
        Query query = entityManager.createNamedQuery(User.QUERY_SELECT_AFTER_USERNAME);
        query.setParameter("username", username);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BusinessException("Invalid Username", "msg-003");
        }
    }

    public List<User> getAllUser() {
        Query query = entityManager.createNamedQuery(User.QUERY_SELECT_ALL_USER, User.class);
        List<User> userList = query.getResultList();
        return userList;
    }


    //This function delete a User and all related activities(Notification, Bugs, Comments) and should
    // not be used in the context of the specification instead user the deactivateUser function.
    //ToDo refactor functia de stergere
    public Integer deleteUserAfterUserNamePermanently(String username) throws BusinessException {
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

    public User login(String username, String password) throws BusinessException {
        Query query = entityManager.createNamedQuery(User.QUERY_USER_LOGIN_AFTER_USERNAME_PASSWORD);
        query.setParameter("username", username);
        query.setParameter("password", password);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BusinessException("login Failed", "msg-001");
        }
    }

    public void activateUser(User user) {
        user.setStatus(true);
    }

    public void deactivateUser(User user){
        user.setStatus(false);
    }


    public User resetLoginFailCounter(User user) throws BusinessException {
        user.setCounter(0);
        return user;
    }




    public User updateUser(User newDataUser) throws BusinessException {
        User user = findeUserAfterUsername(newDataUser.getUsername());
        user.setCounter(newDataUser.getCounter());
        user.setFirstName(newDataUser.getFirstName());
        user.setLastName(newDataUser.getLastName());
        user.setEmail(newDataUser.getEmail());
        user.setMobileNumber(newDataUser.getMobileNumber());
        user.setPassword(newDataUser.getPassword());
        user.setStatus(newDataUser.getStatus());
        return user;

    }


    public List<Bug> getAllCreatedBugs(User user) {
        return user.getCreatedBy();
    }

    public List<Bug> getAllAssignedBugs(User user) {
        return user.getAssignedTo();
    }


    public void addRoleToUser(User userinp, Role role) {
        User user = entityManager.find(User.class, userinp.getId());
        user.addRole(role);
        entityManager.merge(role);
        entityManager.flush();
    }

    public List<Permission> findUserPermissions(String username) {
        TypedQuery<Permission> query = entityManager.createNamedQuery(User.GET_USER_PERMISSIONS, Permission.class);
        query.setParameter("username", username);
        List<Permission> resultList = query.getResultList();
        return resultList;
    }
}
