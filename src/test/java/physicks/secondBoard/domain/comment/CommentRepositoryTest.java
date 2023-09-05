package physicks.secondBoard.domain.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.post.PostRepository;
import physicks.secondBoard.domain.user.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    private Long SAMPLE_POST_ID;
    private Long SAMPLE_MEMBER_ID_1;
    private Long SAMPLE_MEMBER_ID_2;
    private Long SAMPLE_COMMENT_ID_1;
    private Long SAMPLE_COMMENT_ID_2;
    private Long SAMPLE_COMMENT_ID_2_1;
    //
    // @BeforeEach
    // void addSampleComments() {
    //     // 댓글 달 post
    //     Post samplePost
    //             = Post.of("SampleTitle", Author.ofGuest("SampleAuthor", "password"), "SampleContent Hello");
    //     postRepository.save(samplePost);
    //     SAMPLE_POST_ID = samplePost.getId();
    //
    //     // Member
    //     Member member1 = Member.of("loginidkim","passwordkim","kim","kim@gmail.com");
    //     Member member2 = Member.of("loginidpark","passwordpark","park","park@naver.com");
    //     memberRepository.save(member1);
    //     memberRepository.save(member2);
    //     SAMPLE_MEMBER_ID_1 = member1.getId();
    //     SAMPLE_MEMBER_ID_2 = member2.getId();
    //
    //     // 댓글
    //     Comment comment1 = Comment.of("parent1 content", Author.ofMember(member1), samplePost, null);
    //     Comment comment2 = Comment.of("parent2 content", Author.ofMember(member2), samplePost, null);
    //     Comment comment2_1 = Comment.of("parent2-reply1 content", Author.ofMember(member1), samplePost, comment2);
    //     commentRepository.save(comment1);
    //     commentRepository.save(comment2);
    //     commentRepository.save(comment2_1);
    //     SAMPLE_COMMENT_ID_1 = comment1.getId();
    //     SAMPLE_COMMENT_ID_2 = comment2.getId();
    //     SAMPLE_COMMENT_ID_2_1 = comment2_1.getId();
    // }
    //
    // /**
    //  * 샘플 댓글 출력을 위한 테스트
    //  */
    // @Test
    // void printComments() {
    //     List<Comment> all =
    //             commentRepository.findAll();
    //
    //     for (Comment comment : all) {
    //         System.out.println("comment = " + comment + " jpa id = " + comment.getId());
    //     }
    //
    //     assertThat(all.size()).isGreaterThan(1);
    // }
    //
    // // ------ C R U D 테스트 ------
    // @Test
    // void 댓글_CREATE_READ() throws NotFoundException {
    //     // given
    //     Post post = postRepository.findById(SAMPLE_POST_ID)
    //             .orElseThrow(PostNotFoundException::new);
    //     Member member1 = memberRepository.findById(SAMPLE_MEMBER_ID_1)
    //             .orElseThrow(UserNotFoundException::new);
    //     Member member2 = memberRepository.findById(SAMPLE_MEMBER_ID_2)
    //             .orElseThrow(UserNotFoundException::new);
    //
    //     // when
    //     Comment comment = Comment.of("new Comment", Author.ofMember(member1), post, null);
    //     commentRepository.save(comment);
    //
    //     // then
    //     Comment findComment = commentRepository.findById(comment.getId())
    //             .orElseThrow(CommentNotFoundException::new);
    //
    //     assertThat(findComment).isEqualTo(comment);
    // }
    //
    // @Test
    // void 댓글_UPDATE() throws NotFoundException {
    //     // given
    //     Comment comment = commentRepository.findById(SAMPLE_COMMENT_ID_1)
    //             .orElseThrow(CommentNotFoundException::new);
    //     LocalDateTime beforeLastUpdatedTime = comment.getLastUpdatedTime();
    //
    //     // when
    //     comment.update("changed content");
    //     em.flush();
    //     em.clear();
    //
    //     // then
    //     Comment findComment = commentRepository.findById(SAMPLE_COMMENT_ID_1)
    //             .orElseThrow(CommentNotFoundException::new);
    //     assertThat(findComment.getContent()).isEqualTo("changed content");
    //     assertThat(findComment.getLastUpdatedTime()).isNotEqualTo(beforeLastUpdatedTime);
    // }
    //
    // @Test
    // void 댓글_DELETE() throws NotFoundException {
    //     // given
    //     Comment comment = commentRepository.findById(SAMPLE_COMMENT_ID_1)
    //             .orElseThrow(CommentNotFoundException::new);
    //
    //     // when
    //     commentRepository.delete(comment);
    //     em.flush();
    //     em.clear();
    //
    //     // then
    //     Comment deletedComment = commentRepository.findById(SAMPLE_COMMENT_ID_1)
    //             .orElseThrow(CommentNotFoundException::new);
    //
    //     // soft delete 경우 equals() overwriting 에 따라 equal 이 true 이다
    //     assertThat(deletedComment).isEqualTo(comment);
    //     assertThat(deletedComment.getIsDeleted()).isTrue();
    // }

}
