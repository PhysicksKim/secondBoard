package physicks.secondBoard.domain.board;

import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.util.BoardTimeFormatter;

public class PostReadDtoMapper {
    public static PostReadDto toDto(Post post) {
        return new PostReadDto(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getAuthorName(),
                post.getContent(),
                BoardTimeFormatter.forPostRead(post.getCreatedTime())
        );
    }
}
