package physicks.secondBoard.domain.oauth;

import lombok.Getter;
import physicks.secondBoard.domain.user.Member;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    // private String picture;

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        // this.picture = user.getPicture();
    }

    @Override
    public String toString() {
        return "SessionUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
