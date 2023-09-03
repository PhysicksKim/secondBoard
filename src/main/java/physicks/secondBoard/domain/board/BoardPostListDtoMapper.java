package physicks.secondBoard.domain.board;

import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.exception.NullMappingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BoardPostListDtoMapper {

    static public BoardPostListDto toDto(Post post) {
        if(post == null) {
            throw new NullMappingException();
        }

        String formattedCreatedTime = formatCreatedTime(post.getCreatedTime());
        BoardPostListDto dto = new BoardPostListDto(
                post.getId(),
                post.getTitle(),
                post.getAuthor(),
                formattedCreatedTime
        );
        return dto;
    }

    // public Post toEntity(BoardPostListDto dto) {
    //     return null;
    // }

    static private String formatCreatedTime(LocalDateTime createdTime) {
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
