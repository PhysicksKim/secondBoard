package physicks.secondBoard.domain.boardList;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.entity.BaseEntity;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PostListDto {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdTime;

    @Builder
    public PostListDto(Long id,
                       String title,
                       String author,
                       LocalDateTime createdTime) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdTime = createdTime;
    }
}
