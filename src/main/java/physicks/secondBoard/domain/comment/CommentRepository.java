package physicks.secondBoard.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.parentComment = :parentComment ORDER BY c.createdTime")
    List<Comment> findRepliesByParentComment(@Param("parentComment") Comment parentComment);
}
