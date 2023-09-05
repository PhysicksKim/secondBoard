package physicks.secondBoard.domain.author;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import physicks.secondBoard.domain.entity.AuditBaseEntity;
import physicks.secondBoard.domain.user.Guest;
import physicks.secondBoard.domain.user.Member;
import physicks.secondBoard.domain.user.Role;
import physicks.secondBoard.domain.user.User;
import physicks.secondBoard.exception.AuthorRoleException;
import physicks.secondBoard.exception.EntityConstraintViolation;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author extends AuditBaseEntity {

    @Column(nullable = false)
    protected Boolean isGuest;

    @ManyToOne
    @JoinColumn(nullable = true)
    protected Member user;

    @Column(nullable = false)
    protected String nickName;

    @Getter
    @Column(nullable = true, length = 64)
    protected String password;

    public boolean isGuest() {
        return isGuest;
    }

    public void updateNickname(String nickName) {
        if(isGuest) { // 비회원 게시글인 경우에만 글 닉네임 수정 가능
            this.nickName = nickName;
        }
    }

    public String getAuthorName() {
        if(isGuest == false && user == null) {
            throw new AuthorUserNullException("작성자(Author)의 회원 객체(User)가 NULL 입니다");
        }
        return isGuest ? nickName : user.getNickName();
    }

    /**
     * 비회원(guest) 인 경우 user 필드가 null 이여아 합니다. <br>
     * 회원(User) 인 경우 user 필드에 객체가 있어야(Not Null) 합니다.
     */
    @PostPersist
    @PostUpdate
    public void checkConstraints() {
        if ((isGuest && user != null) || (!isGuest && user == null)) {
            throw new EntityConstraintViolation("Author 의 비회원/회원 제약조건에 문제가 생겼습니다");
        }
    }

    public static Author of(User user) {
        Author author;
        if(user.getRole().equals(Role.GUEST)) {
            author = ofGuest(user.getNickName(), user.getPassword());
        } else if(user.getRole().equals(Role.MEMBER)) {
            author = ofMember((Member) user);
        } else {
            throw new AuthorRoleException();
        }

        return author;
    }

    public static Author ofGuest(Guest guest) {
        Author author = new Author();
        author.isGuest = true;
        author.nickName = guest.getNickName();
        author.password = guest.getPassword();

        return author;
    }

    public static Author ofGuest(String nickName, String password) {
        Author author = new Author();
        author.isGuest = true;
        author.nickName = nickName;
        author.password = password;

        return author;
    }

    public static Author ofMember(Member member) {
        Author author = new Author();
        author.isGuest = false;
        author.nickName = member.getNickName();

        return author;
    }
}
