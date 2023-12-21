package physicks.secondBoard.domain.post.author;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import physicks.secondBoard.baseentity.AuditBaseEntity;
import physicks.secondBoard.domain.user.Member;
import physicks.secondBoard.domain.user.Role;
import physicks.secondBoard.domain.user.User;
import physicks.secondBoard.exception.EntityConstraintViolation;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Guest Author : isGuest = true, user = null, password = Not null
 * Member Author : isGuest = false, user = Not null, password = null
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Author extends AuditBaseEntity {

    @Column(nullable = false)
    protected Boolean isGuest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    protected Member member;

    @Column(nullable = false)
    protected String name;

    @Getter
    @Column(nullable = true)
    protected String password;

    public boolean isGuest() {
        return isGuest;
    }

    public void updateName(String name) {
        if(isGuest) { // 비회원 게시글인 경우에만 글 닉네임 수정 가능
            this.name = name;
        }
    }

    public String getAuthorName() {
        if(!isGuest && member == null) {
            throw new IllegalArgumentException("회원 작성자(Author) 객체인데, 회원 필드(Member)가 NULL 입니다");
        }
        return isGuest ? name : member.getName();
    }

    /**
     * 비회원(guest) 인 경우 user 필드가 null 이여아 합니다. <br>
     * 회원(User) 인 경우 user 필드에 객체가 있어야(Not Null) 합니다.
     */
    @PostPersist
    @PostUpdate
    protected void checkConstraints() {
        if ((isGuest && member != null) || (!isGuest && member == null)) {
            throw new EntityConstraintViolation("Author 의 비회원/회원 제약조건에 문제가 생겼습니다. isGuest : " + isGuest + ", user : " + member);
        }
    }

    @Deprecated(since = "명시적으로 Guest, Member 를 구분한 팩토리 메서드를 사용해 주세요")
    public static Author of(User user) {
        Author author;
        if(user.getRole().equals(Role.GUEST)) {
            author = ofGuest(user.getName(), user.getPassword());
        } else if(user.getRole().equals(Role.MEMBER)) {
            author = ofMember((Member) user);
        } else {
            throw new IllegalArgumentException("Author 의 Role 이 GUEST, MEMBER 가 아닙니다. Role : " + user.getRole());
        }

        return author;
    }

    public static Author ofGuest(String name, String password) {
        Author author = new Author();
        author.isGuest = true;
        author.name = name;
        author.password = password;

        return author;
    }

    public static Author ofMember(Member member) {
        Author author = new Author();
        author.isGuest = false;
        author.name = member.getName();
        author.member = member;
        author.password = null;

        return author;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        if(isGuest) {
            return Objects.equals(getId(), author.getId());
        } else {
            return member.equals(author.member);
        }
    }

    @Override
    public String toString() {
        return "Author{" +
                "isGuest=" + isGuest +
                ", user=" + member +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
