package physicks.secondBoard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import physicks.secondBoard.domain.member.login.CustomAuthenticationSuccessHandler;

@Configuration
public class SecurityBeans {

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return getCustomAuthenticationSuccessHandler();
    }

    private SavedRequestAwareAuthenticationSuccessHandler getSavedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler authSuccessHandler
                = new SavedRequestAwareAuthenticationSuccessHandler();

        authSuccessHandler.setDefaultTargetUrl("/"); // 별 다른 설정이 없으면 로그인 성공 후 이동할 url
        authSuccessHandler.setTargetUrlParameter(SecurityConstants.LOGIN_REDIRECT_PARAM); // 쿼리 파라미터로 redirect_uri 를 받아서 로그인 후 이동할 url 을 설정할 수 있음.
        return authSuccessHandler;
    }

    private CustomAuthenticationSuccessHandler getCustomAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

}
