package physicks.secondBoard.domain.board;

import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.exception.NullMappingException;
import physicks.secondBoard.util.BoardTimeFormatter;

public class BoardPostDtoMapper {

    static public BoardPostDto toDto(Post post) {
        if(post == null) {
            throw new NullMappingException();
        }

        String formattedCreatedTime
                = BoardTimeFormatter.forPostRead(post.getCreatedTime());
        BoardPostDto dto = new BoardPostDto(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getAuthorName(),
                formattedCreatedTime
        );
        return dto;
    }

    // public Post toEntity(BoardPostDto dto) {
    //
    //     return null;
    // }

}
