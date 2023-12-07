package physicks.secondBoard.exception;

/**
 * Entity 비즈니스 제약조건을 위반한 경우 발생합니다.
 */
public class EntityConstraintViolation extends RuntimeException {

    public EntityConstraintViolation() {
    }

    public EntityConstraintViolation(String message) {
        super(message);
    }
}
