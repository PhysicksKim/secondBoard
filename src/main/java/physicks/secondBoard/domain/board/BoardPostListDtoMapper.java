package physicks.secondBoard.domain.board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;
import physicks.secondBoard.domain.post.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <h1></>Post - DTO Mapper</h1> <br>
 * Post → BoardPostListDto Mapping에 사용합니다 <br>
 * 글 목록 페이지에 사용합니다. <br>
 * Post에서 id, title, author, createdTime 만 추려냅니다. <br>
 */
@Mapper(componentModel = "spring")
public abstract class BoardPostListDtoMapper {

    @Mapping(
            target = "createdTime",
            expression = "java(formatCreatedTime(post.getCreatedTime()))"
    )
    public abstract BoardPostListDto toDto(Post post);

    // public abstract Post toEntity(BoardPostListDto boardPostListDto);

    public String formatCreatedTime(LocalDateTime createdTime) {
        LocalDateTime now = LocalDateTime.now();

        // 날짜가 동일한 경우 시:분 으로 표기 (14:52)
        if (now.toLocalDate().equals(createdTime.toLocalDate())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return createdTime.format(formatter);
        } else { // 날짜가 다른 경우 월/일 로 표기 (04/23)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
            return createdTime.format(formatter);
        }
    }

    @ObjectFactory
    public Post createPost(BoardPostListDto dto) {
        return Post.of(dto.getTitle(), dto.getAuthor(), "");
    }
}
