package physicks.secondBoard.domain.comment;

import physicks.secondBoard.domain.entity.BaseEntity;

import javax.persistence.*;

@Entity
public class CommentClosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "parent_id")
    private Comment parent;

    @ManyToOne
    @Column(name = "child_id")
    private Comment child;

    private int depth;
}
