package physicks.secondBoard.domain.board.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * 글 작성 Request 를 처리하는 DTO 입니다
 * Request -> Controller -> BoardService -> PostService 까지 전달됩니다
 * </pre>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostGuestWriteDto {
    private String title;
    private String author;
    private String password;
    private String content;
}
