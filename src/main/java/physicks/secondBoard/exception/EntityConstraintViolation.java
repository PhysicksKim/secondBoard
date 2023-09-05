package physicks.secondBoard.exception;

public class EntityConstraintViolation extends RuntimeException {

    public EntityConstraintViolation() {
    }

    public EntityConstraintViolation(String message) {
        super(message);
    }
}
