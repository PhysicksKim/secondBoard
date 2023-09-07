package physicks.secondBoard.domain.board.mapper;

import physicks.secondBoard.domain.board.dto.PostReadDto;
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
