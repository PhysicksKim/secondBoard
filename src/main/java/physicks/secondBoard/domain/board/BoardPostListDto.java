package physicks.secondBoard.domain.board;

import lombok.*;

/**
 * Post → BoardPostListDto <br>
 * 글 목록 페이지에 사용합니다. <br>
 * Post에서 id, title, author, createdTime 만 추려냅니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostListDto {
    private Long id;
    private String title;
    private String author;
    private String createdTime;
}