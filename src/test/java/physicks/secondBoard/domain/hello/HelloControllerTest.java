package physicks.secondBoard.domain.hello;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import physicks.secondBoard.config.oauth.CustomOAuth2UserService;
import physicks.secondBoard.domain.user.Role;
import physicks.secondBoard.domain.user.UserRepository;
import physicks.secondBoard.mock.WithMockCustomOAuth2Account;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HelloController.class)
@MockBeans({
        @MockBean(UserRepository.class),
        @MockBean(CustomOAuth2UserService.class)
})
@Slf4j
class HelloControllerTest {

    @Autowired
    protected MockMvc mvc;

    /**
     * when : 권한 없는 상황에서
     * given : 권한 필요 페이지 접근시
     * then : OAuth2 권한 요청 페이지로 리다이랙트
     */
    @Test
    public void 권한없이_hello접근() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/google"));
        // 302 redirect 로 oauth 요청 페이지로 이동해야함
    }

    /**
     * OAuth 테스트 여부 : X
     * 권한 테스트 여부 : O
     *
     * UsernamePasswordAuthenticationToken
     */
    @Test
    @WithMockUser(username = "guest", roles = {"USER"})
    public void 권한만테스트_hello접근() throws Exception {
        ResultActions resultActions = mvc.perform(get("/hello"))
                .andExpect(status().isOk());

        log.info(resultActions.toString());

        // 권한 보유 시 200_ok 로 Hello 페이지를 볼 수 있어야함
    }

    /**
     * SecurityContext 를 생성해서 OAuth2 권한 테스트
     */
    @Test
    @WithMockCustomOAuth2Account(role = Role.USER, registrationId = "google")
    void hello접근_oauth2_MockOAuth2Account사용() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isOk());
    }

    /**
     * SecurityMockMvcRequestPostProcessors 의 메서드 oauth2Login() 사용 테스트
     * : 스프링 시큐리티에서 지원하는 다양한 인증 처리 방식들을 테스트하기 쉽도록 제공한다.
     */
    @Test
    void hello접근_oauth2_SecurityMock사용() throws Exception {
        // oauth2Login() 은 SecurityMockMvcRequestPostProcessors 에 있는 Mock
        mvc.perform(get("/hello").with(oauth2Login()))
                .andExpect(status().isOk());
    }

    /**
     * SecurityMockMvcRequestPostProcessors 의 메서드 oauth2Login() 사용 + attribute 지정
     */
    @Test
    void hello접근_oauth2_SecurityMock사용_인가처리추가적으로() throws Exception {
        mvc.perform(get("/hello")
            .with(oauth2Login()
                // 1. 권한 인가 객체를 파라미터로 전달함
                .authorities(new SimpleGrantedAuthority("ROLE_GUEST"))
                // 2
                .attributes(attributes -> {
                        attributes.put("username", "username");
                        attributes.put("name", "name");
                        attributes.put("email", "my@email");
                        attributes.put("picture", "https://my_picture");
                    })
            ))
            .andExpect(status().isOk());
    }
}