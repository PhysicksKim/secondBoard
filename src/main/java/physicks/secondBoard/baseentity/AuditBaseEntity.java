package physicks.secondBoard.baseentity;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;


/**
 * <h1>미구현</h1> <br><br>
 * 작성자 기록을 남기기 위한 @MappedSuperclass Entity 이다.
 * BaseEntity를 포함한다.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class AuditBaseEntity extends BaseEntity{
}
