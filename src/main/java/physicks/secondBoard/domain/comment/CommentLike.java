package physicks.secondBoard.domain.comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import physicks.secondBoard.domain.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 엔티티 생성을 위해
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User likeUser;

    public CommentLike(Comment comment, User likeUser) {
        this.comment = comment;
        this.likeUser = likeUser;
    }

    public static CommentLike of(Comment comment, User likeUser) {
        return new CommentLike(comment, likeUser);
    }
}

