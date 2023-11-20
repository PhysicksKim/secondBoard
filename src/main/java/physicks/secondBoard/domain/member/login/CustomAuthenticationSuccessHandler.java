package physicks.secondBoard.domain.member.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    // application yml 에서 환경에 맞게 app.baseUrl 값을 가져온다.
    @Value("${app.baseUrl}")
    private String BASE_URL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (isModalLogin(request)) {
            String url = getValidatedRefererUrl(request);
            log.debug("isModalLogin. url = {}", url);
            getRedirectStrategy().sendRedirect(request, response, url);
        } else {
            log.debug("isNotModalLogin");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private boolean isModalLogin(HttpServletRequest request) {
        String modal = request.getParameter("modal");
        return modal != null && modal.equals("true");
    }

    private String getRefererUrl(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if(referer == null) {
            return "/";
        }
        String url = referer.substring(referer.lastIndexOf("/"));
        return url;
    }

    public String getValidatedRefererUrl(HttpServletRequest request) {
        String refererHeader = request.getHeader("Referer");
        log.debug("refererHeader: {}", refererHeader);
        if (refererHeader != null) {
            try {
                // URL 형식이 맞는지 확인
                URL refererUrl = new URL(refererHeader);
                URL baseUrl = new URL(BASE_URL);

                // 동일한 호스트인지 확인
                if (refererUrl.getHost().equals(baseUrl.getHost())) {
                    return refererHeader;
                }
            } catch (MalformedURLException e) {
                // URL 형식이 잘못된 경우
                e.printStackTrace();
            }
        }
        // 유효하지 않은 경우, 기본 URL 로 리디렉션
        return BASE_URL;
    }
}
