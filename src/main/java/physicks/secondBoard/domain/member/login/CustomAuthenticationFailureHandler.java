package physicks.secondBoard.domain.member.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import physicks.secondBoard.config.SecurityConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final String REDIRECT_PARAM = "redirect";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("Login Fail : Exception {}", exception);

        String redirectUrl = request.getParameter(SecurityConstants.LOGIN_REDIRECT_PARAM);

        String failUrl = "/login?error=true";
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            failUrl += "&" + REDIRECT_PARAM + "=" + redirectUrl;
        }

        response.sendRedirect(failUrl);
    }
}
