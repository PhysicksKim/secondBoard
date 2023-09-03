package physicks.secondBoard.exception;

public class NullMappingException extends RuntimeException {
    public NullMappingException() {
    }

    public NullMappingException(String message) {
        super(message);
    }

    public NullMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullMappingException(Throwable cause) {
        super(cause);
    }

    public NullMappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
