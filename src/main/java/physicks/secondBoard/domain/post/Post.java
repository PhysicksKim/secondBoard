package physicks.secondBoard.domain.post;

import lombok.*;
import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.entity.AuditBaseEntity;

import javax.persistence.*;
import java.util.Objects;

// @Data 어노테이션을 붙이면 toString() 이 자동으로 추가되는데,
// JPA 의 Lazy Loading 때문에 순환 참조 문제를 일으킬 수 있다

/**
 * 게시글 엔티티
 * 제목, 작성자, 내용, 작성일, 마지막 수정일
 */
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 엔티티 생성을 위해
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 정적 팩토리를 위해
public class Post extends AuditBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private Author author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private Post(String title, Author author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    /**
     * Setter 를 열어두면 무분별한 수정이 이뤄질 수 있으므로, update 메서드를 별도로 생성.
     */
    public void updateTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateAuthor(String nickname) {
        author.updateNickname(nickname);
    }

    public void updateAuthor(Author author) {
        author.updateNickname(author.getAuthorName());
    }

    public boolean isGuest() {
        return this.author.isGuest();
    }

    /**
     * 주어진 author의 role 이, post 객체의 role과 일치하는지 체크한다.
     *
     * @param author 객체와 Role 일치 여부를 체크할 대상
     * @return 매개변수와 객체의 Role이 일치하면 true, 일치하지 않으면 false
     */
    private boolean isAuthorRoleMatching(Author author) {
        boolean isArgumentGuest = author.isGuest();
        boolean isInstanceGuest = this.isGuest();

        // 둘 다 true 거나, 둘 다 false 일때 올바른 RoleMatching 이다.
        // 따라서 XNOR 로 연산한 결과를 반환한다.
        return isArgumentGuest == isInstanceGuest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(getTitle(), post.getTitle())
                && Objects.equals(getAuthor(), post.getAuthor())
                && Objects.equals(getContent(), post.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getAuthor(), getContent());
    }

    static public Post of(String title, Author author, String content) {
        return new Post(title, author, content);
    }
}