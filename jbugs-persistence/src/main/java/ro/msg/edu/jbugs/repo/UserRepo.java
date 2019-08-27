package ro.msg.edu.jbugs.repo;

import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.Permission;
import ro.msg.edu.jbugs.entity.Role;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.RepositoryException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.ArrayList;
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

    public User findUserByUsername(String username) throws RepositoryException {
        Query query = entityManager.createNamedQuery(User.QUERY_SELECT_AFTER_USERNAME);
        query.setParameter("username", username);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RepositoryException("Invalid Username", "msg-003");
        }
    }

    public List<User> findAllUser() {
        Query query = entityManager.createNamedQuery(User.QUERY_SELECT_ALL_USER, User.class);
        List<User> userList = query.getResultList();
        return userList;
    }


    //This function delete a User and all related activities(Notification, Bugs, Comments) and should
    // not be used in the context of the specification instead user the setStatusFalse function.
    //ToDo refactor functia de stergere
    public Integer deleteUserByUserNamePermanently(String username) throws RepositoryException {
        Integer result = -1;
        User user = findUserByUsername(username);
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
        try {
            Long numberOfOccurences = entityManager.createNamedQuery(User.QUERY_COUNT_USER_NAME_UNIQUE, Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return numberOfOccurences == 0;
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;
        }
    }

    public User findByUsernameAndPassword(String username, String password) throws RepositoryException {
        Query query = entityManager.createNamedQuery(User.QUERY_USER_LOGIN_AFTER_USERNAME_PASSWORD);
        query.setParameter("username", username);
        query.setParameter("password", password);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RepositoryException("findByUsernameAndPassword Failed", "msg-001");
        }
    }

    public void setStatusTrue(User user) {
        user.setStatus(true);
    }

    public void setStatusFalse(User user) {
        user.setStatus(false);
    }


    public User setFailedLoginAttemptToZero(User user) {
        user.setFailedLoginAttempt(0);
        return user;
    }


    public User updateUser(User newDataUser) throws RepositoryException {
        User user = entityManager.merge(newDataUser);
        entityManager.flush();
        return user;

    }


    public List<Bug> findAllCreatedBugs(User user) {
        return user.getCreatedBugs();
    }

    public List<Bug> findAllAssignedBugs(User user) {
        return user.getAssignedBugs();
    }

    public List<Role> getAllRoles(User user) {
        return user.getRoles();
    }


    public void addRoleToUser(User userinp, Role role) {
        User user = entityManager.find(User.class, userinp.getId());
        user.addRole(role);
        entityManager.merge(role);
        entityManager.flush();
    }

    public void deleteRoleFromUser(User userinp, Role role) {
        User user = entityManager.find(User.class, userinp.getId());
        user.deleteRole(role);
        //???
        entityManager.merge(role);
        entityManager.flush();
    }

    public List<Permission> findUserPermissions(String username) throws RepositoryException {
        User user = findUserByUsername(username);
        List<Role> roleList = user.getRoles();
        List<Permission> resultList = new ArrayList<>();
        roleList.forEach(r -> {
            r.getPermissionList().forEach(permission -> resultList.add(permission));
        });
        return resultList;
    }
}
