package physicks.secondBoard.domain.comment;

import physicks.secondBoard.domain.entity.AuditBaseEntity;
import physicks.secondBoard.domain.post.Post;

import javax.persistence.*;

@Entity
public class Comment extends AuditBaseEntity {

    private String content;
    private String author;

    @ManyToOne // 어떤 Post 의 Comment인지 FK 로 Post
    private Post parentPost;

}
