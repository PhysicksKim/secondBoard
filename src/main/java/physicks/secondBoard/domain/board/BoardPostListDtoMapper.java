package physicks.secondBoard.domain.board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
public interface BoardPostListDtoMapper {
    BoardPostListDtoMapper INSTANCE = Mappers.getMapper(BoardPostListDtoMapper.class);

    @Mapping(
            target = "createdTime",
            expression = "java(formatCreatedTime(post.getCreatedTime()))"
    )
    BoardPostListDto toDto(Post post);

    Post toEntity(BoardPostListDto boardPostListDto);

    /**
     * Post 와 DTO 간의 날짜 표현이 다르므로 이를 변환하기 위한 메서드이다. <br>
     * 1. 오늘 작성한 Post의 경우 "시:분" 으로 표기한다. <br>
     * 2. 오늘과 년월일 이 다른 Post의 경우 "월/일" 로 표기한다. <br>
     * 이 메서드는 MapStruct 라이브러리에 의해서 자동으로 수행된다. <br>
     */
    default String formatCreatedTime(LocalDateTime createdTime) {
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
}