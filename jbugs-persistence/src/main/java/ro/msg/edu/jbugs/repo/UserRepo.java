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

    /**
     * Map the user to the database table and add the user
     *
     * @param user user to be added
     * @return the added user
     */
    public User addUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    /**
     * Finds the User with the given id if one exists
     *
     * @param id of the user to be searched
     * @return the user with the given id
     * @throws EntityNotFoundException if no user with the given id is found
     */
    public User findUser(Integer id) throws EntityNotFoundException {
        return entityManager.getReference(User.class, id);
    }

    /**
     * Finds the User with the given username if one exists
     *
     * @param username of the user to be searched
     * @return the user with the given username
     * @throws RepositoryException if no user with the username is found
     */
    public User findUserByUsername(String username) throws RepositoryException {
        Query query = entityManager.createNamedQuery(User.QUERY_SELECT_AFTER_USERNAME);
        query.setParameter("username", username);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RepositoryException("User with the given Username was not found!", "msg - 019");
        }
    }

    /**
     * Gets a list containing all Users
     *
     * @return list of all Users
     */
    public List<User> findAllUser() {
        Query query = entityManager.createNamedQuery(User.QUERY_SELECT_ALL_USER, User.class);
        return (List<User>) query.getResultList();
    }

    /**
     * Checks if there are no other Users with the given username
     *
     * @param username to be checked for uniqueness
     * @return true if the given username is unique, false otherwise
     */
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

    /**
     * Finds the User with the given username and password if one exists
     *
     * @param username username of the User to be searched
     * @param password password of the User to be searched
     * @return the user with the given username and password
     * @throws RepositoryException if no user with the given username and password exists
     */
    public User findByUsernameAndPassword(String username, String password) throws RepositoryException {
        Query query = entityManager.createNamedQuery(User.QUERY_USER_LOGIN_AFTER_USERNAME_PASSWORD);
        query.setParameter("username", username);
        query.setParameter("password", password);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RepositoryException("findByUsernameAndPassword Failed", "msg - 001");
        }
    }

    /**
     * Resets the FailedLoginAttempts counter to 0 of the given user
     *
     * @param user whom FailedLoginAttempts counter to be reset
     * @return user whom FailedLoginAttempts counter to be reset
     */
    public User setFailedLoginAttemptToZero(User user) {
        user.setFailedLoginAttempt(0);
        return user;
    }

    /**
     * This function delete a User and all related activities(Notification, Bugs, Comments) and should
     * not be used in the context of the specification instead user the setStatusFalse function.
     *
     * @param username username of the username to be deleted
     * @return -1 if the delete has failed 1 if the user was successfully deleted
     */
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

    /**
     * Updates the data of the given User
     *
     * @param newDataUser to be updated
     * @return the updated User
     * @throws RepositoryException
     */
    public User updateUser(User newDataUser) throws RepositoryException {
        User user = entityManager.merge(newDataUser);
        entityManager.flush();
        return user;
    }

    /**
     * Adds a Role to the given User
     *
     * @param userinp user to which the Role is to be added
     * @param role    to be added
     */
    public void addRoleToUser(User userinp, Role role) {
        User user = entityManager.find(User.class, userinp.getId());
        user.addRole(role);
        entityManager.merge(role);
        entityManager.flush();
    }

    /**
     * Removes a Role from a given User
     *
     * @param userinp user to which the Role is to be removed
     * @param role    to be removed
     */
    public void deleteRoleFromUser(User userinp, Role role) {
        User user = entityManager.find(User.class, userinp.getId());
        user.deleteRole(role);
        entityManager.merge(role);
        entityManager.flush();
    }

    /**
     * Gives all Permissions of a User by the User's username
     *
     * @param username of the User
     * @return all Permissions of the User
     * @throws RepositoryException if there is no User with the given username
     */
    public List<Permission> findUserPermissions(String username) throws RepositoryException {
        User user = findUserByUsername(username);
        List<Role> roleList = user.getRoles();
        List<Permission> resultList = new ArrayList<>();
        roleList.forEach(r -> {
            r.getPermissionList().forEach(permission -> resultList.add(permission));
        });
        return resultList;
    }

    public void setStatusTrue(User user) {
        user.setStatus(true);
    }

    public void setStatusFalse(User user) {
        user.setStatus(false);
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

}
