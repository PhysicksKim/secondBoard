package physicks.secondBoard.domain.boardList;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class BoardPostListDto {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdTime;

    @Builder
    public BoardPostListDto(Long id,
                            String title,
                            String author,
                            LocalDateTime createdTime) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdTime = createdTime;
    }
}
