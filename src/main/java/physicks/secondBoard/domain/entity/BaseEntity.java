package physicks.secondBoard.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 수정일 등록일 기능을 추가해주는 @MappedSuperclass Entity 이다. <br>
 * CreatedTime : 등록일 <br>
 * LastUpdatedTime : 최종 수정일 <br>
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
