package physicks.secondBoard.domain.member.signup;

import lombok.Getter;
import lombok.ToString;
import physicks.secondBoard.validator.ValidPassword;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;

@Getter
@ToString
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
