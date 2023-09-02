package physicks.secondBoard.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 삭제되지 않은 comment를 id로 찾는다. (isDeleted = false)
     */
    @Query("SELECT c FROM Comment c WHERE c.isDeleted = false AND c.id = :id")
    Optional<Comment> findById(@Param("id") Long id);

    /**
     * 삭제 여부와 관계없이 id로 comment를 찾는다.
     */
    @Query("SELECT c FROM Comment c WHERE c.id = :id")
    Optional<Comment> findByIdRegardlessOfDeletion(@Param("id") Long id);

    @Query("SELECT c FROM Comment c WHERE c.parentComment = :parentComment ORDER BY c.createdTime")
    List<Comment> findRepliesByParentComment(@Param("parentComment") Comment parentComment);
}
