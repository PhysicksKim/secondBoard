package physicks.secondBoard.devinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.author.AuthorRepository;
import physicks.secondBoard.web.service.MemberService;
import physicks.secondBoard.web.controller.request.signup.MemberSignupDto;
import physicks.secondBoard.domain.post.Post;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
@Transactional
public class SamplePostInitializer implements CommandLineRunner {

    @Autowired
    private PostDevRepository postDevRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MemberService memberService;

    @Override
    public void run(String... args) throws Exception {
        int numPosts = 20;
        addSamplePosts(numPosts);
        minusCreatedTime1Day();
        addSampleMembers();
    }

    private void addSampleMembers() {
        memberService.signupMember(new MemberSignupDto("tester@test.com", "123456aA!", "kim"));
    }

    private void addSamplePosts(int numPosts) {
        for (int i = 0; i < numPosts; i++) {
            String rawPassword = "password" + i;
            String encodedPassword = passwordEncoder.encode(rawPassword);
            Author tempAuthor = Author.ofGuest("Author" + i, encodedPassword);
            authorRepository.save(tempAuthor);

            Post post = Post.of("TestPost" + i, tempAuthor, "TestContent" + i + "\n비밀번호는 "+rawPassword);
            postDevRepository.save(post);
        }
    }

    /**
     * {@link physicks.secondBoard.util.BoardTimeFormatter} 에 의해서
     * 게시판의 작성일 표시 방식은 오늘 작성된 글인 경우 hh:mm 으로 표기되고,
     * 오늘이 아닌 경우 MM/dd 로 표기됩니다.
     * 따라서 이를 테스트하기 위해 createdTime(작성일)을 -i 만큼 해준 post 들을 생성합니다.
     */
    private void minusCreatedTime1Day() {
        List<Post> all = postDevRepository.findAll(Sort.by(Sort.Direction.DESC, "createdTime"));

        int startIdx = 3;
        for(int i = startIdx ; i < all.size() ; i++) {
            LocalDateTime minusDay = LocalDateTime.now().minusDays(i - startIdx + 1);

            Post post = all.get(i);
            postDevRepository.updateTimestamps(post.getId(), minusDay, minusDay);
        }
    }
}
