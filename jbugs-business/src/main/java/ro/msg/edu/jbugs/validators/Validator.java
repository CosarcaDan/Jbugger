package ro.msg.edu.jbugs.validators;

import ro.msg.edu.jbugs.dto.BugDto;
import ro.msg.edu.jbugs.dto.UserDto;
import ro.msg.edu.jbugs.exceptions.BusinessException;

public class Validator {

    public static void validateUser(UserDto user) throws BusinessException {
        if (!validateName(user.getFirstName())){
            throw new BusinessException("Firstname invalid", "msg - 013");
        }
        if(!validateName(user.getLastName())){
            throw new BusinessException("Lastname invalid", "msg - 014");
        }
        if(!validateDEPhoneNumber(user.getMobileNumber()) && !validateROPhoneNumber(user.getMobileNumber())){
            throw new BusinessException("Phone number invalid", "msg - 015");
        }
        if(!validateEmail(user.getEmail())){
            throw new BusinessException("Invalid Email", "msg - 016");
        }
    }

    public static void validateBug(BugDto bug) throws BusinessException {
        if(!validateDescription(bug.getDescription())) {
            throw new BusinessException("Description to short", "msg - 017");
        }
        if(!validateVersion(bug.getVersion())){
            throw new BusinessException("Version Format Invalid", "msg - 018");
        }
        if(bug.getDescription().length() < 250){
            throw new BusinessException("Description has to be at least 250 Characters long", "msg - 020");
        }
    }


    static boolean validateEmail(String email){
        return email.matches("^[a-zA-Z0-9-_.]*@msggroup\\.com$");
    }

    static boolean validateROPhoneNumber(String phonenumber){
        return phonenumber.matches("^(004|\\+4)?07[0-9]{8}$");
    }

    static boolean validateDEPhoneNumber(String phonenumber){
        return phonenumber.matches("^(\\+49)?1(5[12579]|6[023]|7[0-9])[0-9]{7}$");
    }

    static boolean validateName(String name){
        return name.matches("^[A-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ][a-zA-Zșțăîâäöüßáéóőúű]{1,30}[- ]?[a-zșțăîâäöüáéóőúűßA-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ]{0,30}[a-zșțăîâäöüßáéóőúű]$");
    }

    static boolean validateDescription(String description){
        return description.length() >= 250;
    }

    static boolean validateVersion(String version){
        return version.matches("^(0|[1-9a-zA-Z][0-9a-zA-z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)$");
    }
}
