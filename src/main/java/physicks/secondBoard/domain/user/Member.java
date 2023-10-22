package physicks.secondBoard.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?") // soft delete 수행
public class Member extends User {

    @Column(nullable = false)
    protected String email;

    protected boolean isDeleted;

    protected boolean isOauthUser;

    public Member updateName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Role getRole() {
        return Role.MEMBER;
    }

    public static Member of(String password,
                            String name,
                            String email) {

        Member member = new Member();
        member.name = name;
        member.email = email;

        return member;
    }

    public static Member ofOauth(String name, String email) {
        Member member = new Member();
        member.email = email;
        member.name = name;

        return member;
    }

    @Override
    public String toString() {
        return "Member{" +
                "email='" + email + '\'' +
                ", isDeleted=" + isDeleted +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
