package physicks.secondBoard.domain.signup;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignupForm {
    private String email;
    private String password;
    private String passwordCheck;
    private String username;
    private boolean check;

    private SignupForm(String email, String password, String passwordCheck, String username, boolean check) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
        this.check = check;
    }
}
