package physicks.secondBoard.domain.comment;

import physicks.secondBoard.domain.entity.AuditBaseEntity;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Comment extends AuditBaseEntity {

    @NotNull
    @Size(min = 1, max = 300)
    private String content; // 댓글 내용

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    @Size(min = 2, max = 10)
    private User author; // 댓글 작성자

    @NotNull
    private int depth; // 0 : 최상 부모 댓글 , 1 : 대댓글 (기능 확장을 위해 depth 를 만들어 둔 것임)

    @ManyToOne // 어떤 Post 의 Comment인지 FK 로 Post
    @NotNull
    @JoinColumn(name = "post_id", nullable = false)
    private Post parentPost;

    @ManyToOne
    @JoinColumn(name = "reply_parent", nullable = true)
    private Comment parentComment; // 대댓글 구조를 위한 Comment 참조

    private String reply_depth;
}
