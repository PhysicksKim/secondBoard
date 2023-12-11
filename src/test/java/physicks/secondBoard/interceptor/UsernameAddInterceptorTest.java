package physicks.secondBoard.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import physicks.secondBoard.domain.member.login.FormLoginUser;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
class UsernameAddInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testNotLoggedInUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("username", nullValue()));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "MEMBER")
    public void testLoggedInUser() throws Exception {
        mockMvc.perform(get("/")
                        .with(customAuthenticationToken()))
                .andExpect(model().attributeExists("username"));
    }

    private RequestPostProcessor customAuthenticationToken() {
        Authentication formLoginUserToken = generateFormLoginUserToken();
        return SecurityMockMvcRequestPostProcessors.authentication(formLoginUserToken);
    }

    private Authentication generateFormLoginUserToken() {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails details = new FormLoginUser("tester@test.com","123456aA!", "tester", authorities);
        return UsernamePasswordAuthenticationToken.authenticated(details, details.getPassword(), details.getAuthorities());
    }
}