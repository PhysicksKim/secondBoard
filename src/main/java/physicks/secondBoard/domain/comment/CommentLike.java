package physicks.secondBoard.domain.comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import physicks.secondBoard.domain.user.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 엔티티 생성을 위해
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member likeUser;

    public CommentLike(Comment comment, Member likeUser) {
        this.comment = comment;
        this.likeUser = likeUser;
    }

    public static CommentLike of(Comment comment, Member likeUser) {
        return new CommentLike(comment, likeUser);
    }
}

