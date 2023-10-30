package physicks.secondBoard.domain.member.signup;

public class InvalidSignupException extends RuntimeException {
    public InvalidSignupException() {
    }

    public InvalidSignupException(String message) {
        super(message);
    }

    public InvalidSignupException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSignupException(Throwable cause) {
        super(cause);
    }

    public InvalidSignupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
