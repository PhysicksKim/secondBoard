package physicks.secondBoard.domain.board;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import physicks.secondBoard.domain.post.Post;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-27T20:57:47+0900",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 11.0.15 (Oracle Corporation)"
)
@Component
public class BoardPostListDtoMapperImpl extends BoardPostListDtoMapper {

    @Override
    public BoardPostListDto toDto(Post post) {
        if ( post == null ) {
            return null;
        }

        BoardPostListDto boardPostListDto = new BoardPostListDto();

        boardPostListDto.setId( post.getId() );
        boardPostListDto.setTitle( post.getTitle() );
        boardPostListDto.setAuthor( post.getAuthor() );

        boardPostListDto.setCreatedTime( formatCreatedTime(post.getCreatedTime()) );

        return boardPostListDto;
    }
}
