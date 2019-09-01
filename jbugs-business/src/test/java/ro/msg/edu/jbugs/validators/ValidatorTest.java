package ro.msg.edu.jbugs.validators;

import org.junit.Test;
import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.exceptions.BusinessException;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the validations for the bug
 * and user Entity.
 *
 * @author msg systems AG; team D.
 * @since 19.1.2
 */
public class ValidatorTest {

    @Test(expected = BusinessException.class)
    public void validateUser() throws BusinessException {
        UserDto userDto = new UserDto(1, 0, "Sonya", "Parau",
                "sonyaparau@msg.com", "0040720885258", "test",
                "paraus", true);
        Validator.validateUser(userDto);
        userDto.setFirstName("sonya");
        Validator.validateUser(userDto);
    }

    @Test(expected = BusinessException.class)
    public void validateUserName() throws BusinessException {
        UserDto userDto = new UserDto(1, 0, "sonya", "Parau",
                "sonyaparau@msg.com", "0040720885258", "test",
                "paraus", true);
        Validator.validateUser(userDto);
    }

    @Test(expected = BusinessException.class)
    public void validateUserPhone() throws BusinessException {
        UserDto userDto = new UserDto(1, 0, "Sonya", "Parau",
                "sonyaparau@msg.com", "720885258", "test",
                "paraus", true);
        Validator.validateUser(userDto);
    }

    @Test(expected = BusinessException.class)
    public void validateUserLastName() throws BusinessException {
        UserDto userDto = new UserDto(1, 0, "Sonya", "parau",
                "sonyaparau@msggroup.com", "0720885258", "test",
                "paraus", true);
        Validator.validateUser(userDto);
    }

    @Test
    public void validateUserCorrect() throws BusinessException {
        UserDto userDto = new UserDto(1, 0, "Sonya", "Parau",
                "sonyaparau@msggroup.com", "0040720885258", "test",
                "paraus", true);
        Validator.validateUser(userDto);
    }

    @Test
    public void validateBug() throws BusinessException {
        Date date = new Date();
        String description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, " +
                "sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. " +
                "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. " +
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut " +
                "labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et " +
                "justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem" +
                " ipsum dolor sit amet.";
        BugDto bugDto = new BugDto(1, "Bug", description, "1.2.3", new Timestamp(date.getTime()), "NEW",
                "3.4.5", "CRITICAL", "1", "2");
        Validator.validateBug(bugDto);
    }

    @Test(expected = BusinessException.class)
    public void validateBugDescription() throws BusinessException {
        Date date = new Date();
        String description = "Lorem";
        BugDto bugDto = new BugDto(1, "Bug", description, "1.2.3", new Timestamp(date.getTime()), "NEW",
                "3.4.5", "CRITICAL", "1", "2");
        Validator.validateBug(bugDto);
    }

    @Test(expected = BusinessException.class)
    public void validateBugVersion() throws BusinessException {
        Date date = new Date();
        String description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, " +
                "sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. " +
                "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. " +
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut " +
                "labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et " +
                "justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem" +
                " ipsum dolor sit amet.";
        BugDto bugDto = new BugDto(1, "Bug", description, "1.ii2.3ye", new Timestamp(date.getTime()), "NEW",
                "3ue.4okf.5;k;", "CRITICAL", "1", "2");
        Validator.validateBug(bugDto);
    }

    /**
     * Validates the email of the user.
     * An email is valid if it has the form
     * [address]@msggroup.com
     */
    @Test
    public void validateEmail() {
        String email1 = "john@msggroup.com";
        assertTrue(Validator.validateEmail(email1));
        String email2 = "john.bolt@msggroup.com";
        assertTrue(Validator.validateEmail(email2));
        String email3 = "John_bolt101@msggroup.com";
        assertTrue(Validator.validateEmail(email3));
        String email4 = "John_bolt101@msg.com";
        assertFalse(Validator.validateEmail(email4));
        String email5 = "John bolt@msg.com";
        assertFalse(Validator.validateEmail(email5));
    }

    /**
     * Validates the romanian phone number.
     */
    @Test
    public void validateROPhoneNumber() {
        String phone1 = "0040727349454";
        assertTrue(Validator.validateROPhoneNumber(phone1));
        String phone2 = "0727349454";
        assertTrue(Validator.validateROPhoneNumber(phone2));
        String phone3 = "+40626369464";
        assertFalse(Validator.validateROPhoneNumber(phone3));
        String phone4 = "+4062636946410";
        assertFalse(Validator.validateROPhoneNumber(phone4));
        String phone5 = "0040727r49454";
        assertFalse(Validator.validateROPhoneNumber(phone5));
        String phone6 = "+40727349454";
        assertTrue(Validator.validateROPhoneNumber(phone6));
    }

    /**
     * Validate the german phone number.
     */
    @Test
    public void validateDEPhoneNumber() {
        String phone1 = "+491521234516";
        assertTrue(Validator.validateDEPhoneNumber((phone1)));
        String phone2 = "1607912345";
        assertTrue(Validator.validateDEPhoneNumber(phone2));
        String phone3 = "+491702349567";
        assertTrue(Validator.validateDEPhoneNumber(phone3));
        String phone4 = "+49702349567";
        assertFalse(Validator.validateDEPhoneNumber(phone4));
    }

    /**
     * Validates the name of a person.
     * It must begin with uppercase letter
     * and it should contain only letters
     */
    @Test
    public void validateName() {
        String name1 = "Sonya";
        assertTrue(Validator.validateName(name1));
        String name2 = "sonya";
        assertFalse(Validator.validateName(name2));
        String name3 = "Sonya-Roxana";
        assertTrue(Validator.validateName(name3));
        String name4 = "Sona4";
        assertFalse(Validator.validateName(name4));
        String name5 = "4Sonya";
        assertFalse(Validator.validateName(name5));
        String name6 = "Son5y";
        assertFalse(Validator.validateName(name6));
    }

    /**
     * Validates the description of the bug. The description
     * must have at least 250 characters.
     */
    @Test
    public void validateDescription() {
        String description1 = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, " +
                "sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. " +
                "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. " +
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut " +
                "labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et " +
                "justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est" +
                " Lorem ipsum dolor sit amet.";
        assertTrue(Validator.validateDescription(description1));
        String description2 = "something";
        assertFalse(Validator.validateDescription(description2));
    }

    /**
     * Validates the version of a bug. It has to e of
     * format: xx.xx.xx.
     */
    @Test
    public void validateVersion() {
        String version1 = "1.2.3";
        assertTrue(Validator.validateVersion(version1));
        String version2 = "22.22.33";
        assertTrue(Validator.validateVersion(version2));
        String version3 = "12.gg.92";
        assertTrue(Validator.validateVersion(version3));
        String version4 = "12.gg.t2";
        assertTrue(Validator.validateVersion(version4));
        String version5 = "162.gg.t2";
        assertFalse(Validator.validateVersion(version5));
        String version6 = "63-0/49.340";
        assertFalse(Validator.validateVersion(version6));
    }
}
