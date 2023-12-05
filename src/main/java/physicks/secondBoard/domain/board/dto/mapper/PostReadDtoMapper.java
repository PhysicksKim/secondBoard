package physicks.secondBoard.domain.board.dto.mapper;

import physicks.secondBoard.domain.board.dto.PostReadDto;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.util.BoardTimeFormatter;

public class PostReadDtoMapper {
    public static PostReadDto toDto(Post post) {
        if(post == null) {
            throw new IllegalArgumentException("Mapper 에서 Post 객체가 null 입니다");
        }

        return new PostReadDto(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getAuthorName(),
                post.getContent(),
                BoardTimeFormatter.forPostRead(post.getCreatedTime()),
                post.getAuthor().isGuest()
        );
    }
}
