package physicks.secondBoard.web.controller.request.signup;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import physicks.secondBoard.validator.ValidPassword;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;

/**
 * <pre>
 * - request data example
 * email=test@xxx.com,
 * password=123456a!,
 * passwordCheck=12341235123a!,
 * username=kim,
 * check=true
 * </pre>
 */
@Getter
@ToString
@NoArgsConstructor
@Setter
public class SignupForm {

    @Email
    private String email;
    @ValidPassword
    private String password;
    private String passwordCheck;

    private String name;

    @AssertTrue
    private boolean check;

    public SignupForm(String email, String password, String passwordCheck, String name, boolean check) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.name = name;
        this.check = check;
    }
}
