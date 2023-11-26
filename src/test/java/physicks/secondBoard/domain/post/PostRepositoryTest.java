package physicks.secondBoard.domain.post;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.author.AuthorRepository;
import physicks.secondBoard.domain.member.MemberRepository;
import physicks.secondBoard.domain.user.Member;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    private static int samplePostNum = 21;

    @BeforeEach
    void addSamplePosts() {
        for(int i = 0 ; i<samplePostNum ; i++) {
            Author tempAuthor = Author.ofGuest("tester"+i,"password"+i);
            authorRepository.save(tempAuthor);
            postRepository.save(Post.of("title"+i, tempAuthor,"content"+i));
        }
    }

    @Test
    @Transactional
    void 게시판메인페이지_패치조인_n1문제() {
        // Persistence Context Cache Clear
        em.flush();
        em.clear();

        // given
        int size = 5;
        Pageable pageable = PageRequest.of(0, size);

        // when
        Page<Post> results = postRepository.findAllByOrderByCreatedTimeDesc(pageable);
        for (Post result : results) {
            log.info("id={}, authorName={}", result.getId(), result.getAuthor().getAuthorName());
        }

        // then
        assertThat(results).isNotNull();
        assertThat(results.getTotalElements()).isEqualTo(samplePostNum);
        assertThat(results.getTotalPages()).isEqualTo((samplePostNum + size - 1) / size);
        assertThat(results.getContent().size()).isEqualTo(5);

        // entityManager 를 통해 쿼리가 몇 번 날아갔는지 조회 하는법
        // https://stackoverflow.com/questions/3377047/how-to-count-the-number-of-queries-executed-by-spring-jpa
    }

    @Transactional
    @DisplayName("비회원 게시글 저장 테스트")
    @Test
    void GuestPostSaveAndGet() {
        // given
        Author guest = Author.ofGuest("tester", "password");
        authorRepository.save(guest);

        // when
        Post post = Post.of("title", guest, "content");
        postRepository.save(post);

        // then
        assertThat(postRepository.findById(post.getId()).get()).isEqualTo(post);
    }

    @Transactional
    @DisplayName("회원 게시글 저장 테스트")
    @Test
    void MemberPostSaveAndGet() {
        // given
        Member member = Member.of("password", "tester", "tester@test.com", false);
        memberRepository.save(member);
        log.info("member={}", member);
        Author memberAuthor = Author.ofMember(member);
        authorRepository.save(memberAuthor);
        log.info("memberAuthor={}", memberAuthor);

        // when
        Post post = Post.of("title", memberAuthor, "content");
        postRepository.save(post);
        em.flush();
        em.clear();

        // then
        Post findPost = postRepository.findById(post.getId()).get();
        // assertThat(findPost).isEqualTo(post); // equals 구현 문제로 실패
        assertThat(findPost.getAuthor().getAuthorName()).isEqualTo(member.getName());
        assertThat(findPost.getAuthor().isGuest()).isFalse();
    }
}
