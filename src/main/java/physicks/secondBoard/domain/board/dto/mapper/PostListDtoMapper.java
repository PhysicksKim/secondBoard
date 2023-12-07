package physicks.secondBoard.domain.board.dto.mapper;

import physicks.secondBoard.domain.board.dto.PostListDto;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.util.BoardTimeFormatter;

public class PostListDtoMapper {

    static public PostListDto toDto(Post post) {
        if(post == null) {
            throw new IllegalArgumentException("Mapper 에서 Post 객체가 null 입니다");
        }

        String formattedCreatedTime
                = BoardTimeFormatter.forPostRead(post.getCreatedTime());
        PostListDto dto = new PostListDto(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getAuthorName(),
                formattedCreatedTime
        );
        return dto;
    }
}
