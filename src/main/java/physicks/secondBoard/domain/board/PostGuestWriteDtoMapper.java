package physicks.secondBoard.domain.board;

import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.post.Post;

public class PostGuestWriteDtoMapper {

    public static PostGuestWriteDto toDto(Post post) {
        Author author = post.getAuthor();
        return new PostGuestWriteDto(
                post.getTitle(),
                author.getAuthorName(),
                author.getPassword(),
                post.getContent()
        );
    }

}
