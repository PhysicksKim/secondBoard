package physicks.secondBoard.domain.user;

/**
 * 사용되지 않습니다. Guest 객체를 별도로 둘 이유가 현재 없습니다.
 */
@Deprecated
public class Guest extends User {

    public Guest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public Role getRole() {
        return Role.GUEST;
    }
}
