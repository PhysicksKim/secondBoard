package physicks.secondBoard.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.author.Author;
import physicks.secondBoard.domain.user.Member;
import physicks.secondBoard.web.service.BoardService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(controllers = BoardController.class)
class BoardControllerWebMvcTest {

    // board main
    private static final String URL_MAIN = "/board";
    private static final String PAGE_MAIN = "pages/board/board";

    // board write page
    private static final String URL_WRITE = URL_MAIN + "/write";
    private static final String PAGE_WRITE = "pages/board/write";

    // board update page
    private static final String URL_UPDATE = URL_MAIN + "/write/%d";
    private static final String PAGE_UPDATE = PAGE_WRITE;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @DisplayName("메인 페이지는 GUEST 권한으로 접근 가능하다")
    @Test
    @WithMockUser(roles = "GUEST")
    void mainPage() throws Exception{
        mockMvc.perform(get(URL_MAIN))
                .andExpect(status().isOk())
                .andExpect(view().name(PAGE_MAIN));
    }

    @DisplayName("작성 페이지는 GUEST 권한으로 접근 가능하다")
    @Test
    @WithMockUser(roles = "GUEST")
    void postWritePage() throws Exception{
        mockMvc.perform(get(URL_WRITE))
                .andExpect(status().isOk())
                .andExpect(view().name(PAGE_WRITE));
    }

    @DisplayName("수정 토큰 없이 게시글 수정 페이지에 접근하면 권한이 없어서 404 에러가 발생한다")
    @Test
    @WithMockUser(roles = "GUEST") // 게시판은 Guest 권한으로 접근 가능
    void postUpdatePage() throws Exception{
        Long postId = 1L;

        mockMvc.perform(get(String.format(URL_UPDATE, postId)))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("회원 게시글 작성 요청을 보내고, 게시글 읽기 페이지로 리다이렉트 된다")
    @Test
    @WithMockUser(username = "tester", roles = "USER")
    public void whenMemberPostWrite_thenRedirectToPostDetail() throws Exception {
        // given
        mockMvc = withSecurityMockMvc();

        Member member = Member.of("test", "tester", "tester@test.com", false);
        Author author = Author.ofMember(member);

        String title = "Example Title";
        String content = "Example content";
        Post post = Post.of(title, author, content);

        long postId = 1L;
        ReflectionTestUtils.setField(post, "id", postId);

        when(boardService.writeMemberPost(any(),any()))
                .thenReturn(post);

        // when / then
        mockMvc.perform(post("/board/write/test")
                        .param("title", "Example Title")
                        .param("content", "Example content")
                        // .with(authentication(createAuthentication()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/"+postId))
                .andDo(print());
    }

    @DisplayName("비회원 게시글 작성 요청을 보내고, 게시글 읽기 페이지로 리다이렉트 된다")
    @Test
    @WithAnonymousUser
    public void whenGuestPostWrite_thenRedirectToPostDetail() throws Exception {
        // given
        mockMvc = noSecurityMockMvc();

        Author author = Author.ofGuest("Guest", "123456aA!");

        String title = "Example Title";
        String content = "Example content";
        Post post = Post.of(title, author, content);

        long postId = 1L;
        ReflectionTestUtils.setField(post, "id", postId);

        when(boardService.writeGuestPost(any())).thenReturn(post);

        // when / then
        mockMvc.perform(post("/board/write/test")
                        .param("title", "Example Title")
                        .param("authorName", "Guest")
                        .param("password", "password123")
                        .param("content", "Example content")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/" + postId));
    }

    private MockMvc noSecurityMockMvc() {
        return MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    private MockMvc withSecurityMockMvc() {
        return MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }
}
