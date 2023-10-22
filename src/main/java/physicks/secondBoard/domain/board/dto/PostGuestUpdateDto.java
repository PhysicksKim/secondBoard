package physicks.secondBoard.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostGuestUpdateDto {
    private String title;
    private String name;
    private String content;
}
