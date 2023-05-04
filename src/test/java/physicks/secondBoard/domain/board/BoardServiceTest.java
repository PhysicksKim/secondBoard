package physicks.secondBoard.domain.board;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import physicks.secondBoard.domain.post.Post;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void post_saveAndFind() throws Exception {
        //given
        Post post = new Post("title", "author", "content");

        //when
        Post savedPost = boardService.savePost(post);
        Post foundPost = boardService.findPostById(savedPost.getId());

        //then
        assertThat(savedPost).isNotNull();
        assertThat(foundPost).isNotNull();

        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(foundPost.getAuthor()).isEqualTo(post.getAuthor());
        assertThat(foundPost.getContent()).isEqualTo(post.getContent());
    }

}