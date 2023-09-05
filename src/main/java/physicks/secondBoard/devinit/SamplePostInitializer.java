package physicks.secondBoard.devinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.author.AuthorRepository;
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
    private AuthorRepository authorRepository;

    @Override
    public void run(String... args) throws Exception {
        int numPosts = 20;
        addSamplePosts(numPosts);
        minusCreatedTime1Day();
    }

    private void addSamplePosts(int numPosts) {
        for (int i = 0; i < numPosts; i++) {
            Author tempAuthor = Author.ofGuest("Author" + i, "password"+i);
            authorRepository.save(tempAuthor);

            Post post = Post.of("TestPost" + i, tempAuthor, "TestContent" + i);
            postDevRepository.save(post);
        }
    }

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
