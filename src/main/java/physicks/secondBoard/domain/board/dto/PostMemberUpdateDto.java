package physicks.secondBoard.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import physicks.secondBoard.domain.author.Author;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostMemberUpdateDto {
    private String title;
    private Author author;
    private String content;
}
