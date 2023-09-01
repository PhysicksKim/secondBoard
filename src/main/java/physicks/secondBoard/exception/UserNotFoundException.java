package physicks.secondBoard.exception;

public class UserNotFoundException extends NotFoundException{

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long id) {
        super("User with id={" + id + "} NOT FOUND");
    }
}
