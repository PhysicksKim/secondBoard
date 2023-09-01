package physicks.secondBoard.exception;

public class CommentNotFoundException extends NotFoundException{

    public CommentNotFoundException() {
        super();
    }

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(Long id) {
        super("Comment with id={" + id + "} NOT FOUND");
    }
}
