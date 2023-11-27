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
        // Referer 또는 기타 방식으로 이전 페이지 URL을 얻음
        String failureUrl = "/login?error=true";
        String refererUrl = request.getHeader("Referer");
        String redirectUrl = request.getParameter(REDIRECT_PARAM);

        String addUrl = addRedirectUrl(refererUrl, redirectUrl);

        log.info("AuthFail :: referer = {}", refererUrl);
        log.info("AuthFail :: requestURI = {}", request.getRequestURI());
        log.info("AuthFail :: addUrl = {}", addUrl);

        failureUrl += addUrl;
        response.sendRedirect(failureUrl);
    }

    /**
     * A. 이전 페이지 /login && redirect url 이 존재 : &redirect=(redirectUrl)
     * B. 이전 페이지 /login && redirect url 이 존재하지 않음 : ""
     * C. 이전 페이지 /login 아님 : &redirect=(refererUrl)
     * @param refererUrl
     * @param redirectUrl
     * @return
     */
    private String addRedirectUrl(String refererUrl, String redirectUrl) {
        final String redirectKey = "&" + REDIRECT_PARAM + "=";
        // 이전 페이지 정보가 없는 경우 early return
        if(refererUrl == null || refererUrl.isEmpty()) {
            return "";
        }
        // Case A,B : 이전 페이지가 login 페이지인 경우
        if(refererUrl.contains("/login")) {
            if(redirectUrl != null) {
                return redirectKey + redirectUrl; // Case A
            } else {
                return ""; // Case B
            }
        }
        // Case C : 이전 페이지가 login 페이지가 아닌 경우
        return redirectKey + refererUrl;
    }
}
