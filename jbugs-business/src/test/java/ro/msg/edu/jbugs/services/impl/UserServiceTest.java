package ro.msg.edu.jbugs.services.impl;

import com.google.common.hash.Hashing;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.entity.User;
import ro.msg.edu.jbugs.exceptions.BuisnissException;
import ro.msg.edu.jbugs.repo.UserRepo;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import java.nio.charset.StandardCharsets;

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


    @Test
    public void generateUserName() {
        when(userRepo.isUsernameUnique(anyString())).thenReturn(true);
        Assert.assertEquals("cosard", userService.generateUserName("Cosarca", "Dan"));
        verify(userRepo, times(1)).isUsernameUnique(anyString());
    }

    @Test
    public void isUsernameUniqueTest() {
        when(userRepo.isUsernameUnique("baloz")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozs")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozso")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozsol")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozsolt")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozsoltx")).thenReturn(false);
        when(userRepo.isUsernameUnique("balozsoltxx")).thenReturn(true);
        assertEquals("balozsoltxx", userService.generateUserName("Balo", "Zsolt"));
    }

    @Test(expected = BuisnissException.class)
    public void loginTestFailed() throws BuisnissException {
        when(userRepo.login("username1", Hashing.sha256().hashString("password1", StandardCharsets.UTF_8).toString())).thenThrow(BuisnissException.class);
        UserDto userDto = new UserDto(0, 0, "null", "null", "null", "null", "password1", "username1", true);
        userService.login(userDto);
    }

    @Test
    public void loginTestSucces() {
        try {
            User userToReturn = defaultUserToBeTestd();
            when(userRepo.login("username2", Hashing.sha256().hashString("password2", StandardCharsets.UTF_8).toString())).thenReturn(userToReturn);
            UserDto userDto = new UserDto(0, 0, "null", "null", "null", "null", "password2", "username2", true);
            UserDto userDtoReturned = userService.login(userDto);
            assertEquals(userDto.getUsername(),userDtoReturned.getUsername());
        } catch (BuisnissException e) {
            fail();
        }
    }

    private User defaultUserToBeTestd(){
        User user = new User();
        user.setUsername("username2");
        return user;
    }
}