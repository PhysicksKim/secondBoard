package physicks.secondBoard.domain.member.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import physicks.secondBoard.util.RefererUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SavedRequestAwareAuthenticationSuccessHandler 를 상속해서 로그인 성공 시, redirect url 작업을 처리한다.
 * Bean 으로 RefererUtil 을 주입받아서 사용한다.
 * @see physicks.secondBoard.util.RefererUtil
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
        String redirectUrl = request.getParameter("redirect");

        log.info("redirectUrl = {}", redirectUrl);
        if(redirectUrl != null && !redirectUrl.isEmpty()) {
            log.debug("query parameter redirectUrl = {}", redirectUrl);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else if (isModalLogin(request)) {
            String url = refererUtil.getRefererUrl(request);
            log.debug("isModalLogin. url = {}", url);
            getRedirectStrategy().sendRedirect(request, response, url);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private boolean isModalLogin(HttpServletRequest request) {
        String modal = request.getParameter("modal");
        return modal != null && modal.equals("true");
    }

}
