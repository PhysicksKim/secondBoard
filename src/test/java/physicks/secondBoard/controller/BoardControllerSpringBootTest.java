package physicks.secondBoard.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.board.service.BoardService;
import physicks.secondBoard.domain.board.dto.PostGuestWriteDto;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
            PostGuestWriteDto postGuestWriteDto = new PostGuestWriteDto("title" + i, "tester" + i,"password"+i, "content" + i);
            boardService.savePost(postGuestWriteDto);
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
                .andExpect(model().attribute("post", hasProperty("author", is(post.getAuthor().getAuthorName()))))
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
        assertThat(post.getAuthor().getAuthorName()).isEqualTo(author);
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    public void updatePost() throws Exception {
        // 본래라면 url에 path Variable에 있는 post id를 바탕으로 수정 request를 보내게 된다.
        // 이 과정을 생략하고 게시글 수정 자체만 테스트하기 위해서
        // given 에서 그냥 0번째 게시글을 가져온다.

        // given
        // : 수정할 Post 를 하나 가져온다.
        Pageable pageable = PageRequest.of(0,1);
        Post targetPost = postRepository.findAll(pageable).getContent().get(0);
        Long targetPostId = targetPost.getId();

        // when
        // : 게시글 수정 사항 및 mockMvc 수행
        final String title_updated = "updated_" + targetPost.getTitle();
        final String author_updated = "updated_" + targetPost.getAuthor().getAuthorName();
        final String content_updated = "updated_" + targetPost.getContent();

        mockMvc.perform(post(getURL_EDIT(targetPostId))
                .param("id", targetPostId.toString())
                .param("title", title_updated)
                .param("author", author_updated)
                .param("content", content_updated))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/board/" + targetPostId));

        // then
        // : 수정한 post id 를 통해 db에서 다시 가져와서 제대로 수정됐는지 비교
        // JPA 영속성 컨텍스트 때문에 캐싱될 수 있으므로
        // 확실히 DB에서 가져오도록 하기 위해서 영속성 캐시를 초기화 해준다
        em.flush();
        em.clear();

        Post afterUpdated = postRepository.findById(targetPostId).get();
        assertThat(afterUpdated.getTitle()).isEqualTo(title_updated);
        assertThat(afterUpdated.getAuthor().getAuthorName()).isEqualTo(author_updated);
        assertThat(afterUpdated.getContent()).isEqualTo(content_updated);
    }

    private final String getURL_EDIT(Long id) {
        return URL_MAIN + "/write/" + id.toString();
    }

}
