package physicks.secondBoard.devinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

@Component
@Profile("dev")
public class SamplePostInitializer implements CommandLineRunner {

    @Autowired
    private PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {
        int numPosts = 20;
        addSamplePosts(numPosts);
    }

    private void addSamplePosts(int numPosts) {
        for(int i = 0 ; i<numPosts ; i++) {
            Post post = Post.of("TestPost" + i, "Author" + i, "TestContent" + i);
            postRepository.save(post);
        }
    }
}
