package physicks.secondBoard.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLoginDto {
    private String email;
    private String password;
}
