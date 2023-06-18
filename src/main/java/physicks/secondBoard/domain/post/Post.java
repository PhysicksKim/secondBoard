package physicks.secondBoard.domain.post;

import lombok.*;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    public void update(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(getTitle(), post.getTitle()) && Objects.equals(getAuthor(), post.getAuthor()) && Objects.equals(getContent(), post.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getAuthor(), getContent());
    }

    static public Post of(String title, String author, String content) {
        return new Post(title, author, content);
    }
}
