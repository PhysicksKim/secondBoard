package physicks.secondBoard.domain.user;


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
