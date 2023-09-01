package physicks.secondBoard.exception;

public class PostNotFoundException extends NotFoundException{

    public PostNotFoundException() {
        super();
    }

    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(Long id) {
        super("Post with id={" + id + "} NOT FOUND");
    }
}
