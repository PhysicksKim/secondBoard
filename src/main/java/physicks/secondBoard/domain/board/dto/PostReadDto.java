package physicks.secondBoard.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostReadDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private String createdTime;
}
