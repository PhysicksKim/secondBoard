package physicks.secondBoard.web.controller.request.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSignupDto {

    private String email;
    private String password;
    private String name;

}
