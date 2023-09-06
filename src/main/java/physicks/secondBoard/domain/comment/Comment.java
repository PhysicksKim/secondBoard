package physicks.secondBoard.domain.comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.entity.AuditBaseEntity;
import physicks.secondBoard.domain.post.Post;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 엔티티 생성을 위해
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE id = ?") // soft delete 수행
public class Comment extends AuditBaseEntity {

    @NotNull
    @Size(min = 1, max = 300)
    private String content; // 댓글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private Author author; // 댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY) // 어떤 Post 의 Comment 인지 FK 로 Post
    @NotNull
    @JoinColumn(name = "post_id", nullable = false)
    private Post parentPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_parent", nullable = true)
    private Comment parentComment; // 대댓글 구조를 위한 Comment 참조

    @NotNull
    private Integer reply_depth;

    @NotNull
    private Boolean isDeleted; // soft delete 를 위한 필드

    private Comment(String content,
                    Author author,
                    Post parentPost,
                    Comment parentComment,
                    Integer reply_depth) {
        this.content = content;
        this.author = author;
        this.parentPost = parentPost;
        this.parentComment = parentComment;
        this.reply_depth = reply_depth;
        this.isDeleted = false;
    }

    public static Comment of(String content, Author author, Post parentPost, Comment parentComment) {
        int depth = 0;
        if(parentComment != null) {
            depth = parentComment.getReply_depth() + 1;
        }

        return new Comment(content, author, parentPost, parentComment, depth);
    }

    public void update(String content) {
        this.content = content;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    /**
     * 댓글의 내용까지 포함하여 동일한지를 반환한다.
     */
    public boolean equalsWithContent(Comment comment) {
        return this.equals(comment) && Objects.equals(this.content, comment.getContent());
    }

    // ------ Override ------

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getParentComment(), getCreatedTime());
    }

    /**
     *
     * 댓글 내용은 제외하고 id, parentComment, createdTime 만 일치 여부를 검사한다.
     * 이는 댓글이 생성, 수정, 삭제 되는 과정에서 내용이 변화할 수 있는데,
     * 변경이 이뤄지더라도 해당 댓글의 정체성이 동일한지를 검증하기 위해 사용된다.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // getClass 는 정확히 같은 클래스, instance of 는 상속 관계도 true
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;
        return Objects.equals(getId(), comment.getId())
                && Objects.equals(getParentPost(), comment.getParentPost())
                && Objects.equals(getParentComment(), comment.getParentComment())
                && Objects.equals(getCreatedTime(), comment.getCreatedTime());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", author=" + author +
                ", parentPost=" + (parentPost != null ? parentPost.getId() : null) +
                ", parentComment=" + (parentComment != null ? parentComment.getId() : null) +
                ", reply_depth=" + reply_depth +
                '}';
    }
}
