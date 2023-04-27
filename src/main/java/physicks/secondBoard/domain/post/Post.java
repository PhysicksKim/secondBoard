package physicks.secondBoard.domain.post;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import physicks.secondBoard.entity.AuditBaseEntity;
import physicks.secondBoard.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

// @Data 어노테이션을 붙이면 toString() 이 자동으로 추가되는데,
// JPA 의 Lazy Loading 때문에 순환 참조 문제를 일으킬 수 있다

/**
 * 게시글 엔티티
 * 제목, 작성자, 내용, 작성일, 마지막 수정일
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AuditBaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

}
