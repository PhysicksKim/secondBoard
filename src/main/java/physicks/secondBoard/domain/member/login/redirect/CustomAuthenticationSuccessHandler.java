package physicks.secondBoard.domain.member.login.redirect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SavedRequestAwareAuthenticationSuccessHandler 를 상속해서 로그인 성공 시, redirect url 작업을 처리한다.
 * Bean 으로 RefererUtil 을 주입받아서 사용한다.
 * @see RefererUtil
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RefererUtil refererUtil;

    /**
     * Modal 로그인과 Login page 로그인 2가지 방식이 있다.
     * redirect url 채택 우선 순위는 1. 쿼리 파라미터 2. referer(modal 로그인 시) 3. default url(/) 이다.
     * modal 로그인이 아닌 경우에 referer 를 사용하지 않도록 하기 위해서, isModalLogin() 메서드를 통해 구분한다.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        sendLoginSuccessRedirect(request, response, authentication);
    }

    private void sendLoginSuccessRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = request.getParameter("redirect");

        // A. redirectUrl 이 존재한 경우, Redirect URL 로 보낸다.
        if(redirectUrl != null && !redirectUrl.isEmpty()) {
            log.debug("query parameter redirectUrl = {}", redirectUrl);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
        // B. Modal login 인 경우 referer 를 사용한다.
        else if (isModalLogin(request)) {
            String url = refererUtil.getRefererUrl(request);
            log.debug("isModalLogin. url = {}", url);
            getRedirectStrategy().sendRedirect(request, response, url);
        }
        // C. redirectUrl 정보가 없고 Modal login 도 아닌 경우, Saved Request 사용해서 redirect 를 보낸다
        else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    /**
     * Modal Login 으로 한번에 성공한 경우인지 판별한다.
     * 즉 '/login' 페이지에서 온 요청이 아닌경우를 판별하고, Referer 기반으로 redirect 를 수행한다.
     * @param request
     * @return
     */
    private boolean isModalLogin(HttpServletRequest request) {
        String modal = request.getParameter("modal");
        return modal != null && modal.equals("true");
    }

}
