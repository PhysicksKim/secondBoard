package physicks.secondBoard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
public class SecurityBeans {
    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler SavedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler authSuccessHandler
                = new SavedRequestAwareAuthenticationSuccessHandler();

        authSuccessHandler.setDefaultTargetUrl("/");
        authSuccessHandler.setTargetUrlParameter(SecurityConstants.LOGIN_REDIRECT_PARAM);
        return authSuccessHandler;
    }
}
