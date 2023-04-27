package physicks.secondBoard.domain.board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import physicks.secondBoard.domain.post.Post;

@Mapper(componentModel = "spring")
public interface BoardPostListDtoMapper {
    BoardPostListDtoMapper INSTANCE = Mappers.getMapper(BoardPostListDtoMapper.class);

    BoardPostListDto toDto(Post post);
    Post toEntity(BoardPostListDto boardPostListDto);
}
