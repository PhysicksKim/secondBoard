package physicks.secondBoard.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"author"})
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"author"})
    Page<Post> findAllByOrderByCreatedTimeDesc(Pageable pageable);

    @Deprecated
    @EntityGraph(attributePaths = {"author"})
    Page<Post> findAllByOrderByIdDesc(Pageable pageable);
}
