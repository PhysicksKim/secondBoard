package physicks.secondBoard.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import physicks.secondBoard.domain.board.BoardService;
import physicks.secondBoard.domain.post.Post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(roles = "GUEST")
public class BoardControllerSpringBootTest {

    @Autowired
    BoardService boardService;

    private static int samplePostNum = 10;

    @BeforeEach
    void addSamplePosts() {
        for (int i = 0; i < samplePostNum; i++) {
            boardService.savePost(Post.of("title" + i, "tester" + i, "content" + i));
        }
    }

    @Test
    void postRead() {
        // given

    }

    @Test
    void writePost() {

    }


}
