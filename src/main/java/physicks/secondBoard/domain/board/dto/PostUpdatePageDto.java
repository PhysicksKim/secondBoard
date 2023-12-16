package physicks.secondBoard.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class PostUpdatePageDto {
    private final long id;
    private final String title;
    private final String authorName;
    private final String content;
}
