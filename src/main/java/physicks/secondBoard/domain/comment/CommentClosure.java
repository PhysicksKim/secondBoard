package physicks.secondBoard.domain.comment;

import physicks.secondBoard.domain.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Deprecated
public class CommentClosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Comment parent;

    @OneToOne
    private Comment child;

    private int depth;
}
