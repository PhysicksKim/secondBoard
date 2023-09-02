package physicks.secondBoard.domain.comment;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.board.BoardService;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.user.Role;
import physicks.secondBoard.domain.user.User;
import physicks.secondBoard.domain.user.UserRepository;
import physicks.secondBoard.domain.user.UserService;
import physicks.secondBoard.exception.CommentNotFoundException;
import physicks.secondBoard.exception.NotFoundException;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.crossstore.ChangeSetPersister.*;

/**
 * <pre>
 * Comment Service 수준에서 CRUD Test 한다.
 * Comment 는 게시글 작성, 작성자 정보가 필요하므로 Post, User 에 의존한다.
 * 따라서 PostService, UserService 에 의존한다.
 * </pre>
 */
@Slf4j
@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager em;

    private Long SAMPLE_POST_ID;
    private Long SAMPLE_USER_ID_1;
    private Long SAMPLE_USER_ID_2;
    private Long SAMPLE_COMMENT_ID_1;
    private Long SAMPLE_COMMENT_ID_2;
    private Long SAMPLE_COMMENT_ID_2_1;

    @BeforeEach
    void addSampleComments() throws NotFoundException{
        // 댓글 달 post
        Post samplePost
                = Post.of("SampleTitle", "SampleAuthor", "SampleContent Hello");
        boardService.savePost(samplePost);
        SAMPLE_POST_ID = samplePost.getId();

        // User
        User user1 = new User("kim", "kim@gmail.com", "", Role.USER);
        User user2 = new User("park", "park@naver.com", "", Role.USER);
        userService.saveUser(user1);
        userService.saveUser(user2);
        SAMPLE_USER_ID_1 = user1.getId();
        SAMPLE_USER_ID_2 = user2.getId();

        // 댓글
        Comment comment1 = Comment.of("parent1 content", user1, samplePost, null);
        Comment comment2 = Comment.of("parent2 content", user2, samplePost, null);
        Comment comment2_1 = Comment.of("parent2-reply1 content", user1, samplePost, comment2);
        commentService.saveComment(comment1);
        commentService.saveComment(comment2);
        commentService.saveComment(comment2_1);
        SAMPLE_COMMENT_ID_1 = comment1.getId();
        SAMPLE_COMMENT_ID_2 = comment2.getId();
        SAMPLE_COMMENT_ID_2_1 = comment2_1.getId();
    }

    @Test
    void 댓글작성및조회() throws NotFoundException{
        // given
        Post parentPost = boardService.findPostById(SAMPLE_POST_ID);
        User user = userService.findUserById(SAMPLE_USER_ID_1);

        // when
        Comment comment = Comment.of("new comment content", user, parentPost, null);
        commentService.saveComment(comment);
        em.flush();
        em.clear();

        // then
        Comment findComment = commentService.findCommentById(comment.getId());
        assertThat(comment).satisfies(c -> assertTrue(c.equalsWithContent(findComment)));
    }

    @Test
    void 댓글수정() throws NotFoundException {
        // given
        Comment comment = commentService.findCommentById(SAMPLE_COMMENT_ID_1);
        final LocalDateTime beforeLastUpdatedTime = comment.getLastUpdatedTime();
        final String beforeContent = comment.getContent();

        // when
        comment.update("modified content");
        em.flush();
        em.clear();

        // then
        Comment findComment = commentService.findCommentById(SAMPLE_COMMENT_ID_1);
        assertThat(findComment.getLastUpdatedTime()).isAfter(beforeLastUpdatedTime);
        assertThat(comment).isEqualTo(findComment);
        assertThat(beforeContent).isNotEqualTo(findComment.getContent());
    }

    @Test
    void 댓글삭제() throws NotFoundException {
        // given
        Comment comment = commentService.findCommentById(SAMPLE_COMMENT_ID_1);

        // when
        commentService.deleteComment(comment);

        // then
        assertThrows(NotFoundException.class, () -> commentService.findCommentById(SAMPLE_COMMENT_ID_1));
    }

    // @Test
    // void 댓글좋아요() {}

    // @Test
    // void 댓글좋아요취소() {}

    // ------ 대댓글 ------
    @Test
    void 대댓글_CREATE_READ() throws NotFoundException {
        // given
        Post parentPost = boardService.findPostById(SAMPLE_POST_ID);
        Comment parentComment = commentService.findCommentById(SAMPLE_COMMENT_ID_1);

        User replyUser = User.builder()
                .name("ReplyUser")
                .email("reply@gmail.com")
                .role(Role.USER)
                .build();
        userService.saveUser(replyUser);

        Comment replyComment = Comment.of("reply content", replyUser, parentPost, parentComment);

        // when
        commentService.saveReplyComment(replyComment, parentComment);
        em.flush();
        em.clear();

        // then
        Comment findReplyComment = commentService.findReplyComments(parentComment).get(0);
        assertThat(findReplyComment.getId()).isEqualTo(replyComment.getId());
        assertThat(findReplyComment).isEqualTo(replyComment);

        assertThat(findReplyComment.getParentComment().getId()).isEqualTo(parentComment.getId());
        assertThat(findReplyComment.getParentPost().getId()).isEqualTo(parentPost.getId());
    }

    @Test
    void 대댓글_DELETE() throws NotFoundException {
        // given
        Post parentPost = boardService.findPostById(SAMPLE_POST_ID);
        Comment parentComment = commentService.findCommentById(SAMPLE_COMMENT_ID_2);
        Comment deleteTargetComment = commentService.findCommentById(SAMPLE_COMMENT_ID_2_1);

        // when
        commentService.deleteComment(deleteTargetComment);
        em.flush();
        em.clear();

        // then
        assertThrows(NotFoundException.class, () -> commentService.findCommentById(SAMPLE_COMMENT_ID_2_1));
    }
}