package physicks.secondBoard.domain.comment;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;
import physicks.secondBoard.domain.user.Role;
import physicks.secondBoard.domain.user.User;
import physicks.secondBoard.domain.user.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.data.crossstore.ChangeSetPersister.*;

/**
 * <pre>
 * Comment Repository 수준에서 CRUD Test 한다.
 * Comment 는 게시글 작성, 작성자 정보가 필요하므로 Post, User 에 의존한다.
 * 따라서 PostRepository, UserRepository 에 의존한다.
 * </pre>
 */
@Slf4j
@Transactional
@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private Long SAMPLE_POST_ID;
    private Long SAMPLE_USER_ID_1;
    private Long SAMPLE_USER_ID_2;
    private Long SAMPLE_COMMENT_ID_1;
    private Long SAMPLE_COMMENT_ID_2;
    private Long SAMPLE_COMMENT_ID_2_1;

    @BeforeEach
    void addSampleComments() {
        // 댓글 달 post
        Post samplePost
                = Post.of("SampleTitle", "SampleAuthor", "SampleContent Hello");
        postRepository.save(samplePost);
        SAMPLE_POST_ID = samplePost.getId();

        // User
        User user1 = new User("kim", "kim@gmail.com", "", Role.USER);
        User user2 = new User("park", "park@naver.com", "", Role.USER);
        userRepository.save(user1);
        userRepository.save(user2);
        SAMPLE_USER_ID_1 = user1.getId();
        SAMPLE_USER_ID_2 = user2.getId();

        // 댓글
        Comment comment1 = Comment.of("parent1 content", user1, samplePost, null);
        Comment comment2 = Comment.of("parent2 content", user2, samplePost, null);
        Comment comment2_1 = Comment.of("parent2-reply1 content", user1, samplePost, comment2);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment2_1);
        SAMPLE_COMMENT_ID_1 = comment1.getId();
        SAMPLE_COMMENT_ID_2 = comment2.getId();
        SAMPLE_COMMENT_ID_2_1 = comment2_1.getId();
    }

    @Test
    void printComments() {
        List<Comment> all =
                commentRepository.findAll();

        for (Comment comment : all) {
            System.out.println("comment = " + comment + " jpa id = " + comment.getId());
        }
    }

    // ------ C R U D 테스트 ------
    @Test
    void 댓글_CREATE_READ() throws NotFoundException {
        // given
        Post post = postRepository.findById(SAMPLE_POST_ID)
                .orElseThrow(NotFoundException::new);
        User user1 = userRepository.findById(SAMPLE_USER_ID_1)
                .orElseThrow(NotFoundException::new);
        User user2 = userRepository.findById(SAMPLE_USER_ID_2)
                .orElseThrow(NotFoundException::new);

        // when
        Comment comment = Comment.of("new Comment", user1, post, null);
        commentRepository.save(comment);

        // then
        Comment findComment = commentRepository.findById(comment.getId())
                .orElseThrow(NotFoundException::new);

        assertThat(findComment).isEqualTo(comment);
    }

    @Test
    void 댓글_UPDATE() throws NotFoundException {
        // given
        Comment comment = commentRepository.findById(SAMPLE_COMMENT_ID_1)
                .orElseThrow(NotFoundException::new);
        LocalDateTime beforeLastUpdatedTime = comment.getLastUpdatedTime();

        // when
        comment.update("changed content");
        em.flush();
        em.clear();

        // then
        Comment findComment = commentRepository.findById(SAMPLE_COMMENT_ID_1)
                .orElseThrow(NotFoundException::new);
        assertThat(findComment.getContent()).isEqualTo("changed content");
        assertThat(findComment.getLastUpdatedTime()).isNotEqualTo(beforeLastUpdatedTime);
    }

    @Test
    void 댓글_DELETE() throws NotFoundException {
        // given
        Comment comment = commentRepository.findById(SAMPLE_COMMENT_ID_1)
                .orElseThrow(NotFoundException::new);

        // when
        commentRepository.delete(comment);
        em.flush();
        em.clear();

        // then
        Comment deletedComment = commentRepository.findById(SAMPLE_COMMENT_ID_1)
                .orElseThrow(NotFoundException::new);

        // soft delete 경우 equals() overwriting 에 따라 equal 이 true 이다
        assertThat(deletedComment).isEqualTo(comment);
        assertThat(deletedComment.getIsDeleted()).isTrue();
    }
}
