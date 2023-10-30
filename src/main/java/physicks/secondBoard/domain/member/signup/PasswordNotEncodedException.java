package physicks.secondBoard.domain.member.signup;

public class PasswordNotEncodedException extends RuntimeException {

    public PasswordNotEncodedException() {
    }

    public PasswordNotEncodedException(String message) {
        super(message);
    }

    public PasswordNotEncodedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordNotEncodedException(Throwable cause) {
        super(cause);
    }

    public PasswordNotEncodedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
