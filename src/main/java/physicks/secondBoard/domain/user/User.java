package physicks.secondBoard.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import physicks.secondBoard.baseentity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * User는 비회원/회원 둘 다 가능하다.
 */
@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class User extends BaseEntity {

    @Column(nullable = false)
    protected String name;

    @Column(nullable = true)
    protected String password;

    abstract public Role getRole();
}
