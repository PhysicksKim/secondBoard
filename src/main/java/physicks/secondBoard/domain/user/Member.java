package physicks.secondBoard.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import physicks.secondBoard.web.controller.request.signup.SignupForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 를 위한 기본 생성자
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?") // soft delete 수행
public class Member extends User {

    @Column(nullable = false)
    @NotBlank
    @Email
    protected String email;

    @NotNull
    protected boolean isDeleted = false;

    @NotNull
    protected boolean isOauthUser;

    public Member updateName(String name) {
        this.name = name;
        return this;
    }

    public static Member of(String encodedPassword,
                            String name,
                            String email,
                            boolean isOauthUser) {

        Member member = new Member();
        member.email = email;
        member.password = encodedPassword;
        member.name = name;
        member.isOauthUser = isOauthUser;

        return member;
    }

    public static Member ofSignupDTO(SignupForm dto) {
        Member member = new Member();
        member.email = dto.getEmail();
        member.password = dto.getPassword();
        member.name = dto.getName();

        member.isOauthUser = false;
        return member;
    }

    public static Member ofOauth(String name, String email) {
        Member member = new Member();
        member.email = email;
        member.name = name;

        member.isOauthUser = true;
        return member;
    }

    @Override
    public Role getRole() {
        return Role.MEMBER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (isDeleted() != member.isDeleted()) return false;
        if (isOauthUser() != member.isOauthUser()) return false;
        return getEmail().equals(member.getEmail());
    }

    @Override
    public int hashCode() {
        int result = getEmail().hashCode();
        result = 31 * result + (isDeleted() ? 1 : 0);
        result = 31 * result + (isOauthUser() ? 1 : 0);
        return result;
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
