package physicks.secondBoard.domain.member.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSignupDto {

    private String email;
    private String password;
    private String name;

}
