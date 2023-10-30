package physicks.secondBoard.domain.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import physicks.secondBoard.domain.post.PostRepository;
import physicks.secondBoard.domain.member.MemberRepository;

@DataJpaTest
public class CommentLikeRepositoryTest {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    // @BeforeEach
    // void addSamples() {
    //     Member member1 = Member.of("loginidkim","passwordkim","kim","kim@gmail.com");
    //     Member member2 = Member.of("loginidpark","passwordpark","park","park@naver.com");
    //     memberRepository.save(member1);
    //     memberRepository.save(member2);
    //
    //     Post post1 = Post.of("title1", Author.ofMember(member1), "post Content1");
    //     Post post2 = Post.of("title2", Author.ofMember(member2), "post Content2");
    //     postRepository.save(post1);
    //     postRepository.save(post2);
    //
    //     Comment comment1 = Comment.of("content1", Author.ofMember(member1), post1, null);
    //     Comment comment2 = Comment.of("content2", Author.ofMember(member2), post2, null);
    //     commentRepository.save(comment1);
    //     commentRepository.save(comment2);
    // }
    //
    // @Test
    // void testAddLike() {
    //     // Given
    //     userRepository.save(user);
    //
    //     Comment comment = Comment.of("comment Content", user, ) new Comment(/*... initialize comment fields ... */);
    //     commentRepository.save(comment);
    //
    //     // When
    //     CommentLike commentLike = CommentLike.of(comment, user);
    //     commentLikeRepository.save(commentLike);
    //
    //     // Then
    //     assertThat(commentLikeRepository.existsByCommentAndLikeUser(comment, user)).isTrue();
    // }
    //
    // @Test
    // void testRemoveLike() {
    //     // Given
    //     Comment comment = new Comment(/*... initialize comment fields ... */);
    //     commentRepository.save(comment);
    //     User user = new User(/*... initialize user fields ... */);
    //     userRepository.save(user);
    //
    //     CommentLike commentLike = CommentLike.of(comment, user);
    //     commentLikeRepository.save(commentLike);
    //
    //     // When
    //     commentLikeRepository.delete(commentLike);
    //
    //     // Then
    //     assertThat(commentLikeRepository.existsByCommentAndLikeUser(comment, user)).isFalse();
    // }
    //
    // @Test
    // void testLikeExists() {
    //     // Given
    //     Comment comment = new Comment(/*... initialize comment fields ... */);
    //     commentRepository.save(comment);
    //     User user = new User(/*... initialize user fields ... */);
    //     userRepository.save(user);
    //
    //     CommentLike commentLike = CommentLike.of(comment, user);
    //     commentLikeRepository.save(commentLike);
    //
    //     // When & Then
    //     assertThat(commentLikeRepository.existsByCommentAndLikeUser(comment, user)).isTrue();
    // }
    //
    // @Test
    // void testCountLikes() {
    //     // Given
    //     Comment comment = new Comment(/*... initialize comment fields ... */);
    //     commentRepository.save(comment);
    //
    //     User user1 = new User(/*... initialize user fields ... */);
    //     userRepository.save(user1);
    //     User user2 = new User(/*... initialize user fields ... */);
    //     userRepository.save(user2);
    //
    //     CommentLike commentLike1 = CommentLike.of(comment, user1);
    //     CommentLike commentLike2 = CommentLike.of(comment, user2);
    //
    //     commentLikeRepository.save(commentLike1);
    //     commentLikeRepository.save(commentLike2);
    //
    //     // When & Then
    //     assertThat(commentLikeRepository.countByComment(comment)).isEqualTo(2);
    // }
}
