package physicks.secondBoard.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import physicks.secondBoard.web.service.BoardService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * <pre>
 * WebMvcTest 를 수행합니다.
 * service를 사용해야 하는경우 {@link BoardControllerSpringBootTest} 에서 수행해주세요.
 * 아래 테스트들은 SpringTest 가 더 적합하므로 mock에서 테스트하지 않음.
 * writePost, postRead
 * </pre>
 *
 * <h2>@WithMockUser(roles = "GUEST") 가 필요한 이유</h2>
 *
 * <pre>
 * 게시판 페이지 자체는 가장 낮은 GUEST 권한으로도 접근 가능하다.
 * 그러나 @WithMockUser 가 없으면 Security 가 확인할 Role 자체가 없다.
 * Spring Security 는 권한 자체가 없는 것을 확인하고
 * "너 GUEST도 아니고, ROLE 자체가 없네? 누구야" 하면서 권한 갖고 오라고 Login page로 넘겨버린다.
 * 그러므로 최소한 GUEST 권한이라도 집어 넣어줘야 한다.
 * </pre>
 */
@WebMvcTest(BoardController.class) // class를 지정해주지 않으면 모든 @Controller 들을 로딩해버린다.
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@WithMockUser(roles = "GUEST") // 게시판은 Guest 권한으로 접근 가능
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

    @DisplayName("게시판 메인 페이지는 GUEST 권한으로 접근 가능하다")
    @Test
    void mainPage() throws Exception{
        mockMvc.perform(get(URL_MAIN))
                .andExpect(status().isOk())
                .andExpect(view().name(PAGE_MAIN));
    }

    @DisplayName("게시글 작성 페이지는 GUEST 권한으로 접근 가능하다")
    @Test
    void postWritePage() throws Exception{
        mockMvc.perform(get(URL_WRITE))
                .andExpect(status().isOk())
                .andExpect(view().name(PAGE_WRITE));
    }

    @DisplayName("수정 토큰 없이 게시글 수정 페이지에 접근하면 권한이 없어서 404 에러가 발생한다")
    @Test
    void postUpdatePage() throws Exception{
        Long postId = 1L;

        mockMvc.perform(get(String.format(URL_UPDATE, postId)))
                .andExpect(status().is4xxClientError());
    }
}