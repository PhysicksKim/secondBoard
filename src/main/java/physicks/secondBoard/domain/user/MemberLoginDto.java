package physicks.secondBoard.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLoginDto {
    private String loginId;
    private String rawPassword;
}
