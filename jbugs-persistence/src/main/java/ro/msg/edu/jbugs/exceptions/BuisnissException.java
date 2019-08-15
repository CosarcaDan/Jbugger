package ro.msg.edu.jbugs.exceptions;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class BuisnissException extends Exception {
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorCode;

    public BuisnissException(String s,String errorCode) {
        super(s);
        this.errorCode = errorCode;
    }

    public BuisnissException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
