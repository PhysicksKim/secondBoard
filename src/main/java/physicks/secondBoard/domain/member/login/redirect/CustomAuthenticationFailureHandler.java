package physicks.secondBoard.domain.member.login.redirect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import physicks.secondBoard.config.SecurityConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Thymeleaf 에서 error 메시지를 출력하기 위해 query parameter 에 error=true 를 추가한다.
 * Security 관련 Handler 관리를 위해서 SecurityBeans 에서 Bean 으로 등록한다.
 * @see physicks.secondBoard.config.SecurityBeans
 */
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final String REDIRECT_PARAM = SecurityConstants.LOGIN_REDIRECT_PARAM;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // Referer 또는 기타 방식으로 이전 페이지 URL 을 얻음
        String failRedirectUrl = generateFailRedirectUrl(request);
        response.sendRedirect(failRedirectUrl);
    }

    private String generateFailRedirectUrl(HttpServletRequest request) {
        final String loginPageUrl = "/login";
        return loginPageUrl + '?' + errorParam() + '&' + redirectUrlParam(request);
    }

    private String errorParam() {
        return "error=ture";
    }

    /**
     * A. 이전 페이지 /login && redirect url 이 존재하지 않음 : ""
     * B. 이전 페이지 /login && redirect url 이 존재 : redirect=(redirectUrl)
     * C. 이전 페이지 /login 아님 : redirect=(refererUrl)
     * @param request referer, RedirectParam 을 추출
     * @return
     */
    private String redirectUrlParam(HttpServletRequest request) {
        String redirectUrl = request.getParameter(REDIRECT_PARAM);
        String refererUrl = request.getHeader("Referer");

        StringBuilder result = new StringBuilder(REDIRECT_PARAM);
        result.append('=');

        // 이전 페이지 정보가 없는 경우 early return
        if(refererUrl == null || refererUrl.isEmpty()) {
            return "";
        }
        // Case A,B : 이전 페이지가 login 페이지인 경우
        if(refererUrl.contains("/login")) {
            if(redirectUrl == null) {
                return ""; // Case A
            } else { // redirectUrl != null
                return result.append(redirectUrl).toString(); // Case B
            }
        }
        // Case C : 이전 페이지가 login 페이지가 아닌 경우
        return result.append(refererUrl).toString();
    }
}
