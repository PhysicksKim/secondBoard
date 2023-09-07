package physicks.secondBoard.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRegisterDto {

    private String loginId;
    private String password;
    private String nickname;
    private String email;

}
