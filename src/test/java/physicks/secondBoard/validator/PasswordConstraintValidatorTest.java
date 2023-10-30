package physicks.secondBoard.validator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import physicks.secondBoard.domain.member.signup.SignupForm;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@DisplayName("비밀번호 검증 @어노테이션 테스트")
class PasswordConstraintValidatorTest {

    private final static String EMAIL = "passwordtest@test.com";
    private final static String NAME = "validKim";
    private final static String VALID_PASSWORD = "Valid1234!";
    private final static String INVALID_PASSWORD = "invalid";


    @Autowired
    private Validator validator;

    @Test
    @DisplayName("올바른 비밀번호")
    void valid_Password() {
        // given
        SignupForm dto = new SignupForm(EMAIL, VALID_PASSWORD, VALID_PASSWORD, NAME, true);

        // when
        Set<ConstraintViolation<SignupForm>> validate = validator.validate(dto);

        // then
        log.info("validate length {} ", validate.size());
        for (ConstraintViolation<SignupForm> violation : validate) {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            Annotation annotation = violation.getConstraintDescriptor().getAnnotation();
            log.info("Field name: {}, Error message: {}, Violated Annotation: {}", fieldName, message, annotation.annotationType().getSimpleName());
        }
        assertThat(validate.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("잘못된 비밀번호")
    void invalid_password() {
        // given
        SignupForm dto = new SignupForm(EMAIL, INVALID_PASSWORD, INVALID_PASSWORD, NAME, true);

        // when
        Set<ConstraintViolation<SignupForm>> validate = validator.validate(dto);

        // then
        log.info("validate length {} ", validate.size());
        for (ConstraintViolation<SignupForm> violation : validate) {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            Annotation annotation = violation.getConstraintDescriptor().getAnnotation();
            log.info("Field name: {}, Error message: {}, Violated Annotation: {}", fieldName, message, annotation.annotationType().getSimpleName());
        }

        ConstraintViolation<SignupForm> violation = validate.iterator().next();
        assertThat(validate.size()).isEqualTo(1)
                .withFailMessage( "비밀번호가 유효하지 않아서 1개의 ConstraintViolation이 발생해야 합니다.");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password")
                .withFailMessage("비밀번호 필드에서 ConstraintViolation이 발생해야 합니다.");
        assertThat(violation.getConstraintDescriptor().getAnnotation().annotationType())
                .isEqualTo(ValidPassword.class)
                .withFailMessage("@ValidPassword 어노테이션에 의해 ConstraintViolation이 발생해야 합니다.");
    }
}