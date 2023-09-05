package physicks.secondBoard.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostGuestUpdateDto {
    private String title;
    private String nickname;
    private String content;
}
