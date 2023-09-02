package physicks.secondBoard.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import physicks.secondBoard.domain.user.User;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndLikeUser(Comment comment, User user);

    CommentLike findByCommentAndLikeUser(Comment comment, User user);

    long countByComment(Comment comment);
}
