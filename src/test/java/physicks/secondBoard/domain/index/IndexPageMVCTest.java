package physicks.secondBoard.domain.index;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IndexPageMVCTest {

    private MockMvc mockMvc;

    /**
     * 기본 index 페이지 요청에 응답이 정상적으로 이뤄지는지 테스트
     */
    @Test
    public void indexPage_정상응답_test() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<html")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<body>")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("</body>")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("</html>"))
        );
    }
}
