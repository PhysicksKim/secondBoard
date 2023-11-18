package physicks.secondBoard.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * findAll() 은 성능상 문제로 금지합니다. Pageable 을 사용해주세요.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * findAll() 은 성능상 문제로 금지합니다. Pageable 을 사용해주세요.
     * @return throw RuntimeException
     */
    @Deprecated
    default List<Post> findAll() {
        throw new RuntimeException("findAll() 은 금지되었습니다. Pageable 을 사용해주세요.");
    }

    @EntityGraph(attributePaths = {"author"})
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"author"})
    Page<Post> findAllByOrderByCreatedTimeDesc(Pageable pageable);

    @Deprecated
    @EntityGraph(attributePaths = {"author"})
    Page<Post> findAllByOrderByIdDesc(Pageable pageable);
}
