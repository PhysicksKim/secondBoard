package physicks.secondBoard.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.board.dto.PostWriteGuestDto;
import physicks.secondBoard.web.service.BoardService;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(roles = "GUEST")
@Transactional
public class BoardControllerSpringBootTest {

    private final String URL_MAIN = "/board";

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BoardService boardService;

    private static int samplePostNum = 10;

    // boardService를 사용하게 되면 boardService 기능의 정상적 구현에 더 집중되므로
    // controller 테스트보다는 boardService 테스트에 가까워 진다.
    @BeforeEach
    void addSamplePosts() {
        for (int i = 0; i < samplePostNum; i++) {
            PostWriteGuestDto postWriteGuestDto = new PostWriteGuestDto("title" + i, "tester" + i,"password"+i, "content" + i);
            boardService.writeGuestPost(postWriteGuestDto);
        }
    }

    @DisplayName("게시글 읽기 페이지 request 에 올바른 view 를 반환한다.")
    @Test
    void postRead() throws Exception{
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Post> all = postRepository.findAllByOrderByCreatedTimeDesc(pageRequest).getContent();
        Post post = all.get(0);
        long id = post.getId();

        mockMvc.perform(get("/board/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/board/post"))
                .andExpect(model().attribute("post", hasProperty("title", is(post.getTitle()))))
                .andExpect(model().attribute("post", hasProperty("author", is(post.getAuthor().getAuthorName()))))
                .andExpect(model().attribute("post", hasProperty("content", is(post.getContent()))));
    }

    @DisplayName("Guest 게시글 작성")
    @Test
    void writePost() throws Exception{
        // given
        final String title = "testWriteTitle";
        final String author = "testWriteAuthor";
        final String content = "testWriteContent";
        final String password = "123456aA!";

        // when
        ResultActions perform = mockMvc.perform(post("/board/write") //
                .param("title", title)
                .param("author", author)
                .param("content", content)
                .param("password", password));
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Post> postPage = postRepository.findAllByOrderByCreatedTimeDesc(pageRequest);
        List<Post> postList = postPage.getContent();
        Post post = postList.get(postList.size() - 1);

        // then
        perform.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/" + post.getId()));

        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getAuthor().getAuthorName()).isEqualTo(author);
        assertThat(post.getContent()).isEqualTo(content);
        // password 는 encoded 되므로 NotEqual 이다.
        assertThat(post.getAuthor().getPassword()).isNotEqualTo(password);
    }

}
