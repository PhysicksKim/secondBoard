package physicks.secondBoard.domain.user;


public class Guest extends User {

    public Guest(String nickname, String password) {
        this.nickName = nickname;
        this.password = password;
    }

    @Override
    public Role getRole() {
        return Role.GUEST;
    }
}
