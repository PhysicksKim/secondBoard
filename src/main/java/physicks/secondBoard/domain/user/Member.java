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

    @Column(name = "login_id")
    protected String loginId;

    protected boolean isDeleted;

    public Member updateNickname(String nickname) {
        this.nickName = nickname;
        return this;
    }

    @Override
    public Role getRole() {
        return Role.MEMBER;
    }

    public static Member of(String loginId,
                            String password,
                            String nickname,
                            String email) {

        Member member = new Member();
        member.loginId = loginId;
        member.password = password;
        member.nickName = nickname;
        member.email = email;

        return member;
    }

    public static Member ofOauth(String email, String nickname) {
        Member member = new Member();
        member.email = email;
        member.nickName = nickname;

        return member;
    }
}
