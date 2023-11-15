package physicks.secondBoard.baseEntity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <pre>
 * 수정일 등록일 기능을 추가해주는 @MappedSuperclass Entity 이다.
 * Annotation 기반 Audit 기능을 통해 자동으로 값이 추가되므로 수동으로 값을 넣어줄 필요가 없다.
 * CreatedTime : 등록일
 * LastUpdatedTime : 최종 수정일
 * </pre>
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastUpdatedTime;
}
