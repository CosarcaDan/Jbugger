package ro.msg.edu.jbugs.exceptions;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
public class BusinessException extends Exception {
    public BusinessException(RepositoryException e) {
        super(e);
        this.errorCode = e.getErrorCode();

    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorCode;

    public BusinessException(String s,String errorCode) {
        super(s);
        this.errorCode = errorCode;
    }

    public BusinessException(String s, Throwable throwable) {
        super(s, throwable);
    }

    @Override
    public String toString() {
        return super.getMessage() + " : " + " " + this.errorCode;
    }
}
