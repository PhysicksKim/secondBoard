package physicks.secondBoard.entity;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Auditing 대상 필드
 * CreatedTime : 등록일
 * LastUpdatedTime : 최종 수정일
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class AuditBaseEntity extends BaseEntity{
}
