package physicks.secondBoard.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.board.BoardService;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(roles = "GUEST")
@Transactional
public class BoardControllerSpringBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BoardService boardService;

    @Autowired
    PostRepository postRepository;

    private static int samplePostNum = 10;

    // boardService를 사용하게 되면 boardService 기능의 정상적 구현에 더 집중되므로
    // controller 테스트보다는 boardService 테스트에 가까워 진다.
    @BeforeEach
    void addSamplePosts() {
        for (int i = 0; i < samplePostNum; i++) {
            postRepository.save(Post.of("title" + i, "tester" + i, "content" + i));
        }
    }

    @Test
    void postRead() throws Exception{
        List<Post> all = postRepository.findAll();
        Post post = all.get(0);
        long id = post.getId();

        mockMvc.perform(get("/board/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attribute("post", hasProperty("title", is(post.getTitle()))))
                .andExpect(model().attribute("post", hasProperty("author", is(post.getAuthor()))))
                .andExpect(model().attribute("post", hasProperty("content", is(post.getContent()))));
    }

    @Test
    void writePost() throws Exception{
        // given
        final String title = "testWriteTitle";
        final String author = "testWriteAuthor";
        final String content = "testWriteContent";

        // when
        ResultActions perform = mockMvc.perform(post("/board/write")
                .param("title", title)
                .param("author", author)
                .param("content", content));
        List<Post> postAll = postRepository.findAll();
        Post post = postAll.get(postAll.size() - 1);

        // then
        perform.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/" + post.getId()));

        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.getContent()).isEqualTo(content);
    }
}
