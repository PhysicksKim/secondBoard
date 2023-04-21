package physicks.secondBoard.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import physicks.secondBoard.domain.boardList.PostListDto;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new physicks.secondBoard.domain.boardList.PostListDto(p.id, p.title, p.author, p.createdTime) from Post p")
    public List<PostListDto> findAllPostListDtos();
}
