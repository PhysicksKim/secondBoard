package physicks.secondBoard.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * static 으로 구현 시 BASE_URL 환경 변수 값을 주입받을 수 없기 때문에 Bean 으로 등록해서 사용한다.
 * BASE_URL 은 Referer 추출 과정에서 사용된다.
 * @see physicks.secondBoard.domain.member.login.CustomAuthenticationSuccessHandler
 */
@Slf4j
@Component
public class RefererUtil {

    @Value("${app.baseUrl}")
    private String BASE_URL;

    /**
     * HttpServletRequest 에서 Referer 를 추출할 때 사용한다.
     * Referer 추출 과정에서 발생할 수 있는 예외를 처리하고, 보안을 고려해 호스트 url 과 일치하지 않는 referer 를 걸러낸다.
     * @param request
     * @return
     */
    public String getRefererUrl(HttpServletRequest request) {
        String refererHeader = request.getHeader("Referer");
        log.debug("refererHeader: {}", refererHeader);
        if (refererHeader != null) {
            try {
                // URL 형식이 맞는지 확인
                URL refererUrl = new URL(refererHeader);
                log.info("BASE_URL: {}", BASE_URL);
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
