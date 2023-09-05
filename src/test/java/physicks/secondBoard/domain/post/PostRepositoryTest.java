package physicks.secondBoard.domain.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import physicks.secondBoard.domain.author.Author;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private static int samplePostNum = 21;

    @BeforeEach
    void addSamplePosts() {
        for(int i = 0 ; i<samplePostNum ; i++) {
            postRepository.save(Post.of("title"+i, Author.ofGuest("tester"+i,"password"+i),"content"+i));
        }
    }

    @Test
    void PostRepository_findAll_Pageable_Test() {
        int size = 5;
        Pageable pageable = PageRequest.of(0,size);

        Page<Post> results = postRepository.findAll(pageable);

        assertThat(results).isNotNull();
        assertThat(results.getTotalElements()).isEqualTo(samplePostNum);
        assertThat(results.getTotalPages()).isEqualTo((samplePostNum + size - 1)/size);
        assertThat(results.getContent().size()).isEqualTo(5);
    }
}
