package ro.msg.edu.jbugs.services.impl;

import com.google.common.hash.Hashing;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.dto.mappers.UserDtoMapping;
import ro.msg.edu.jbugs.entity.Bug;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BusinessException;
import ro.msg.edu.jbugs.exceptions.RepositoryException;
import ro.msg.edu.jbugs.repo.RoleRepo;
import ro.msg.edu.jbugs.repo.UserRepo;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepo userRepo;
    public UserServiceTest() {
        this.userService = new UserService();
    }
    @Mock
    private RoleRepo roleRepo;


    @Test
    public void addUserTestSucces() throws IOException, BusinessException {
        UserDto tempUserDto = new UserDto(2, 0, "Fnt", "Lnt", "et@msggroup.com", "+40712345678", Hashing.sha256().hashString("defaultPass", StandardCharsets.UTF_8).toString(), "lntf", true);
        when(userRepo.addUser(UserDtoMapping.userDtoToUser(tempUserDto))).thenReturn(UserDtoMapping.userDtoToUser(tempUserDto));
        when(userRepo.isUsernameUnique("lntf")).thenReturn(true);

        //userService.addUser(tempUserDto);
    }

    @Test(expected = BusinessException.class)
    public void addUserTestFailedFirstnameContainsNumberValidation() throws BusinessException, IOException {
        UserDto tempUserDto = new UserDto(1,1,"Fnt1","lnt","et","mnt","pt","unt",true);
        userService.addUser(tempUserDto);
    }

    @Test(expected = BusinessException.class)
    public void addUserTestFailedFirstnameStartsLowerValidation() throws BusinessException, IOException {
        UserDto tempUserDto = new UserDto(1,1,"fnt","lnt","et","mnt","pt","unt",true);
        userService.addUser(tempUserDto);
    }

    @Test(expected = BusinessException.class)
    public void addUserTestFailedFirstnameEndsUpperValidation() throws BusinessException, IOException {
        UserDto tempUserDto = new UserDto(1,1,"FnT","lnt","et","mnt","pt","unt",true);
        userService.addUser(tempUserDto);
    }
    //-----
    @Test(expected = BusinessException.class)
    public void addUserTestFailedLastnameContainsNumberValidation() throws BusinessException, IOException {
        UserDto tempUserDto = new UserDto(1,1,"Fnt","Lnt1","et","mnt","pt","unt",true);
        userService.addUser(tempUserDto);
    }

    @Test(expected = BusinessException.class)
    public void addUserTestFailedLastnameStartsLowerValidation() throws BusinessException, IOException {
        UserDto tempUserDto = new UserDto(1,1,"Fnt","lnt","et","mnt","pt","unt",true);
        userService.addUser(tempUserDto);
    }

    @Test(expected = BusinessException.class)
    public void addUserTestFailedLastnameEndsUpperValidation() throws BusinessException, IOException {
        UserDto tempUserDto = new UserDto(1,1,"Fnt","LnT","et","mnt","pt","unt",true);
        userService.addUser(tempUserDto);
    }

    @Test(expected = BusinessException.class)
    public void addUserTestFailedPhoneNumberValidation() throws IOException, BusinessException {
        UserDto tempUserDto = new UserDto(1,1,"Fnt","Lnt","et@msggroup.com","07123456789","pt","unt",true);
        userService.addUser(tempUserDto);
    }

    @Test(expected = BusinessException.class)
    public void addUserTestFailedEmailValidation() throws IOException, BusinessException {
        UserDto tempUserDto = new UserDto(1,1,"Fnt","Lnt","et@msg.com","0712345678","pt","unt",true);
        userService.addUser(tempUserDto);
    }



    @Test
    public void findUserTest() throws BusinessException {
        when(userRepo.findUser(123)).thenReturn(new User(1,"fnt","lnt","et","mnt","pt","unt",true));
        UserDto userDto = userService.findUser(123);
        assertEquals((Integer) 1, userDto.getFailedLoginAttempt());
        assertEquals("fnt",userDto.getFirstName());
        assertEquals("lnt",userDto.getLastName());
        assertEquals("et",userDto.getEmail());
        assertEquals("mnt",userDto.getMobileNumber());
        assertEquals("pt",userDto.getPassword());
        assertEquals("unt",userDto.getUsername());
        assertTrue(userDto.getStatus());

    }

    @Test(expected = BusinessException.class)
    public void findUserTestFailed() throws BusinessException{
        when(userRepo.findUser(123)).thenThrow(EntityNotFoundException.class);
        userService.findUser(123);

    }

    @Test
    public void getAllUserTestEmptyList(){
        List<User> tempUserList = new ArrayList<>();
        when(userRepo.findAllUser()).thenReturn(tempUserList);
        assertEquals(0,userService.getAllUser().size());
    }

    @Test
    public void getAllUserTest(){
        List<User> tempUserList = Arrays.asList(new User(),new User(),new User());
        when(userRepo.findAllUser()).thenReturn(tempUserList);
        assertEquals(3,userService.getAllUser().size());
    }

    @Test
    public void getAllCreatedBugsTestSucces() throws BusinessException {
        User tempUser = new User();
        when(userRepo.findUser(123)).thenReturn(tempUser);
        when(userRepo.findAllCreatedBugs(tempUser)).thenReturn(new ArrayList<>());
        assertTrue(userService.getAllCreatedBugs(123).isEmpty());
    }
    @Test(expected = BusinessException.class)
    public void getAllCreatedBugsTestFailed() throws BusinessException {
        when(userRepo.findUser(123)).thenThrow(EntityNotFoundException.class);
        userService.getAllCreatedBugs(123);
    }

    @Test
    public void getAllAssignedBugsTestSucces() throws BusinessException {
        User tempUser = new User();
        when(userRepo.findUser(123)).thenReturn(tempUser);
        when(userRepo.findAllAssignedBugs(tempUser)).thenReturn(new ArrayList<>());
        assertTrue(userService.getAllAssignedBugs(123).isEmpty());

    }

    @Test(expected = BusinessException.class)
    public void getAllAssignedBugsTestFailed() throws BusinessException {
        when(userRepo.findUser(123)).thenThrow(EntityNotFoundException.class);
        userService.getAllAssignedBugs(123);
    }


    @Test
    public void generateUserNameTest() {
        when(userRepo.isUsernameUnique(anyString())).thenReturn(true);
        Assert.assertEquals("cosard", userService.generateUserName("Cosarca", "Dan"));

    }

    @Test
    public void isUsernameUniqueTest() {
        when(userRepo.isUsernameUnique("baloz")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozs")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozso")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozsol")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozsolt")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozsolt0")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozsolt1")).thenReturn(true);
        assertEquals("balozsolt1", userService.generateUserName("Balo", "Zsolt"));
    }

    @Test(expected = BusinessException.class)
    public void loginTestFailedUserAlreadyInactiv() throws BusinessException, RepositoryException {
        when(userRepo.findByUsernameAndPassword("username1", Hashing.sha256().hashString("password1", StandardCharsets.UTF_8).toString())).thenThrow(RepositoryException.class);
        User tempUser = new User();
        tempUser.setFailedLoginAttempt(5);
        tempUser.setStatus(true);
        when(userRepo.findUserByUsername("username1")).thenReturn(tempUser);
        UserDto userDto = new UserDto(0, 0, "null", "null", "null", "null", "password1", "username1", true);
        userService.login(userDto);
    }

    @Test(expected = BusinessException.class)
    public void loginTestFailedUserAboutToBeDeactivated() throws RepositoryException, BusinessException {
        when(userRepo.findByUsernameAndPassword("username1", Hashing.sha256().hashString("password1", StandardCharsets.UTF_8).toString())).thenThrow(BusinessException.class);
        User tempUser = new User();
        tempUser.setFailedLoginAttempt(4);
        when(userRepo.findUserByUsername("username1")).thenReturn(tempUser);
        UserDto userDto = new UserDto(0, 0, "null", "null", "null", "null", "password1", "username1", true);
        userService.login(userDto);
    }
    @Test(expected = BusinessException.class)
    public void loginTestFailed() throws RepositoryException, BusinessException {
        when(userRepo.findByUsernameAndPassword("username1", Hashing.sha256().hashString("password1", StandardCharsets.UTF_8).toString())).thenThrow(BusinessException.class);
        User tempUser = new User();
        tempUser.setFailedLoginAttempt(3);
        when(userRepo.findUserByUsername("username1")).thenReturn(tempUser);
        UserDto userDto = new UserDto(0, 0, "null", "null", "null", "null", "password1", "username1", true);
        userService.login(userDto);
    }

    @Test
    public void loginTestSucces() {
        try {
            User userToReturn = defaultUserToBeTestd();
            when(userRepo.findByUsernameAndPassword("username2", Hashing.sha256().hashString("password2", StandardCharsets.UTF_8).toString())).thenReturn(userToReturn);
            UserDto userDto = new UserDto(0, 0, "null", "null", "null", "null", "password2", "username2", true);
            UserDto userDtoReturned = userService.login(userDto);
            assertEquals(userDto.getUsername(),userDtoReturned.getUsername());
        } catch (RepositoryException | BusinessException e) {
            fail();
        }
    }

    @Test
    public void ActivateUserTestSucces() throws RepositoryException, BusinessException {
        //todo complet user and assert at end if dto == user?
        User tempUser = new User();
        tempUser.setStatus(false);
        when(userRepo.findUserByUsername("username")).thenReturn(tempUser);
        userService.activateUser("username");
        //mby assert on the return userDto. exceptions my appear if temp user is empty

    }

    @Test(expected = BusinessException.class)
    public void ActivateUserFaliedUserAlreadyActive() throws RepositoryException, BusinessException {
        //todo complet user and assert at end if dto == user?
        User tempUser = new User();
        tempUser.setStatus(true);
        when(userRepo.findUserByUsername("username")).thenReturn(tempUser);
        userService.activateUser("username");
        //mby assert on the return userDto. exceptions my appear if temp user is empty

    }

    @Test(expected = BusinessException.class)
    public void ActivateUsertestFailed() throws RepositoryException, BusinessException {
        when(userRepo.findUserByUsername("username")).thenThrow(RepositoryException.class);
        userService.activateUser("username");
    }

    @Test
    public void deactivateUserTestSuccesUserWithNoAssigment() throws BusinessException, RepositoryException {
        User tempUser = new User();
        tempUser.setStatus(true);
        tempUser.setAssignedBugs(new ArrayList<>());
        when(userRepo.findUserByUsername("username")).thenReturn(tempUser);
        //userService.deactivateUser("username", null);
    }

    @Test
    public void deactivateUserTestSuccesUserWithClosedBugs() throws BusinessException, RepositoryException {
        User tempUser = new User();
        tempUser.setStatus(true);
        Bug bug1 = new Bug();
        bug1.setStatus(Bug.Status.CLOSED);
        Bug bug2 = new Bug();
        bug2.setStatus(Bug.Status.CLOSED);
        tempUser.setAssignedBugs(Arrays.asList(bug1, bug2));
        when(userRepo.findUserByUsername("username")).thenReturn(tempUser);
        //userService.deactivateUser("username", null);
    }

    @Test(expected = BusinessException.class)
    public void deactivateUserTestFailedInvalidUsername() throws RepositoryException, BusinessException {
        when(userRepo.findUserByUsername("username")).thenThrow(BusinessException.class);
        userService.deactivateUser("username", null);
    }

    @Test(expected = BusinessException.class)
    public void deactivateUserTestFailedUserAlreadyClosed() throws RepositoryException, BusinessException {
        User tempUser = new User();
        tempUser.setStatus(false);
        when(userRepo.findUserByUsername("username")).thenReturn(tempUser);
        userService.deactivateUser("username", null);
    }

    public void deactivateUserTestFailedBugsNotClosed() throws RepositoryException, BusinessException {
        //when(roleRepo.findAdminRole().getUserList().isEmpty()).thenReturn(true);
        //when(roleRepo.findAdminRole()).thenReturn(null);
        User tempUser = new User();
        tempUser.setStatus(true);
        Bug bug1 = new Bug();
        bug1.setStatus(Bug.Status.CLOSED);
        Bug bug2 = new Bug();
        bug2.setStatus(Bug.Status.IN_PROGRESS);
        tempUser.setAssignedBugs(Arrays.asList(bug1, bug2));
        when(userRepo.findUserByUsername("username")).thenReturn(tempUser);
        //userService.deactivateUser("username", null);
    }

    @Test
    public void deleteUserTestSucces() throws RepositoryException, BusinessException {
        when(userRepo.deleteUserByUserNamePermanently("username")).thenReturn(1);
        UserDto tempUserDto = new UserDto();
        tempUserDto.setUsername("username");
        assertEquals((Integer)1,userService.deleteUser(tempUserDto));
    }

    @Test(expected = BusinessException.class)
    public void deleteUserTestFailed() throws RepositoryException, BusinessException {
        when(userRepo.deleteUserByUserNamePermanently("username")).thenThrow(BusinessException.class);
        UserDto tempUserDto = new UserDto();
        tempUserDto.setUsername("username");
        userService.deleteUser(tempUserDto);
    }


    @Test
    public void updateUserSucces() throws BusinessException, RepositoryException {
        UserDto tempUserDto = new UserDto(1,1,"Fnt","Lnt","et@msggroup.com","+40712345678","pt","unt",true);
        when(userRepo.updateUser(UserDtoMapping.userDtoToUser(tempUserDto))).thenReturn(UserDtoMapping.userDtoToUser(tempUserDto));
        assertEquals(tempUserDto,userService.updateUser(tempUserDto));
    }

    @Test(expected = BusinessException.class)
    public void updateUserFailed() throws BusinessException, RepositoryException {
        UserDto tempUserDto = new UserDto(1,1,"Fnt","Lnt","et@msggroup.com","+40712345678","pt","unt",true);
        when(userRepo.updateUser(UserDtoMapping.userDtoToUser(tempUserDto))).thenThrow(BusinessException.class);
        userService.updateUser(tempUserDto);
    }



    private User defaultUserToBeTestd(){
        User user = new User();
        user.setUsername("username2");
        return user;
    }
}
