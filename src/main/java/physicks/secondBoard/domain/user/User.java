package physicks.secondBoard.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import physicks.secondBoard.domain.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * User는 비회원/회원 둘 다 가능하다.
 */
@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class User extends BaseEntity {

    @Column(nullable = false)
    protected String nickName;

    @Column(nullable = true)
    protected String password;

    abstract public Role getRole();

}
