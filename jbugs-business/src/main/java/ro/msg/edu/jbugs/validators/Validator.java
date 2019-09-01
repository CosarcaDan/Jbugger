package ro.msg.edu.jbugs.validators;

import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.exceptions.BusinessException;

/**
 * Validates the user and bug entity.
 */
public class Validator {

    /**
     * Validates the first name, last name and mobile number
     * of the user.
     */
    public static void validateUser(UserDto user) throws BusinessException {
        if (!validateName(user.getFirstName())){
            throw new BusinessException("Firstname invalid", "msg - 006");
        }
        if(!validateName(user.getLastName())){
            throw new BusinessException("Lastname invalid", "msg - 007");
        }
        if(!validateDEPhoneNumber(user.getMobileNumber()) && !validateROPhoneNumber(user.getMobileNumber())){
            throw new BusinessException("Phone number invalid", "msg - 008");
        }
        if(!validateEmail(user.getEmail())){
            throw new BusinessException("Invalid Email", "msg - 009");
        }
    }

    /**
     * Validates the length of the description and the version
     * of the bug.
     */
    public static void validateBug(BugDto bug) throws BusinessException {
        if(!validateDescription(bug.getDescription())) {
            throw new BusinessException("Description too short", "msg - 012");
        }
        if(!validateVersion(bug.getVersion())){
            throw new BusinessException("Version Format Invalid", "msg - 013");
        }
        if (bug.getDescription().length() < 250) {
            throw new BusinessException("Description too", "msg - 012");
        }
    }

    /**
     * Email must be of fromat [address]@msggroup.com
     * */
    static boolean validateEmail(String email){
        return email.matches("^[a-zA-Z0-9-_.]*@msggroup\\.com$");
    }

    /**
     * Checks if the given number has a romanian format.
     * */
    static boolean validateROPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^(004|\\+4)?07[0-9]{8}$");
    }

    /**
     * Checks if the given number has a german format.
     * */
    static boolean validateDEPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^(\\+49)?1(5[12579]|6[023]|7[0-9])[0-9]{7}$");
    }

    /**
     * Validates the name of the user.
     * */
    static boolean validateName(String name){
        return name.matches("^[A-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ][a-zA-Zșțăîâäöüßáéóőúű]{0,30}[- ]?" +
                "[a-zșțăîâäöüáéóőúűßA-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ]{0,30}[a-zșțăîâäöüßáéóőúű]$");
    }

    /**
     * Validates the length of the description.
     * */
    static boolean validateDescription(String description){
        return description.length() >= 250;
    }

    /**
     * Validates the version of the bug.
     * */
    static boolean validateVersion(String version){
        return version.matches("^(0|[1-9a-zA-Z][0-9a-zA-z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)$");
    }
}
