package physicks.secondBoard.domain.board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import physicks.secondBoard.domain.post.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface BoardPostListDtoMapper {
    BoardPostListDtoMapper INSTANCE = Mappers.getMapper(BoardPostListDtoMapper.class);

    @Mapping(
            target = "createdTime",
            expression = "java(formatCreatedTime(post.getCreatedTime()))"
    )
    BoardPostListDto toDto(Post post);

    Post toEntity(BoardPostListDto boardPostListDto);

    default String formatCreatedTime(LocalDateTime createdTime) {
        LocalDateTime now = LocalDateTime.now();

        // 날짜가 동일한 경우 14:52 같이 시:분 으로 표기
        if (now.toLocalDate().equals(createdTime.toLocalDate())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return createdTime.format(formatter);
        } else { // 날짜가 다른 경우 04/23 같이 월/일 로 표기
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
            return createdTime.format(formatter);
        }
    }
}