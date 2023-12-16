package physicks.secondBoard.domain.member.login.redirect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 사용자가 unAuthorized 상태에서 권한이 필요한 페이지로 접근 시 동작합니다.
 * 권한 필요 페이지로 접근하는 경우, Spring 에서 제공하는 SavedRequestAwareAuthenticationSuccessHandler 를 사용해서
 * RequestCache 를 활용해서 Authentication Success 이후 요청했던 페이지로 redirect 합니다.
 * 따라서 Redirect Url 을 따로 저장하지 않아도 됩니다.
 * 전체적인 동작 순서는 다음과 같습니다.
 * <pre>
 * 1. 권한 필요 페이지로 접근
 * 2. Authentication 관련 Filter 가 동작.
 *   ex. UsernamePasswordAuthenticationFilter.doFilter() 에서 requiresAuthentication() 를 통해 권한 필요 페이지인지 체크하고 동작함
 * 3. 권한 필요 페이지라면 Filter 별로 정의된 체크 로직이 수행됨. 이 과정에서 사용자가 권한이 없으므로 (비로그인) AuthenticationException 발생
 * 4. ExceptionTranslationFilter 가 이 Exception 을 catch 하여 AuthenticationEntryPoint 를 호출함
 *   - ExceptionTranslationFilter 에서 AuthenticationException 발생 시, Request 를 cache 해 둠
 *   ex. ExceptionTranslationFilter.handleSpringSecurityException() -> sendStartAuthentication() 에서 AuthenticationEntryPoint.commence() 를 호출함
 * 5. AuthenticationEntryPoint.commence() 에 의해서 로그인 페이지로 redirect 됨
 * </pre>
 * 위 순서에 따라 "권한 필요 페이지에 비로그인으로 접근한 경우, 로그인 페이지로 redirect" 가 이뤄진다.
 */
@Slf4j
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * AuthenticationException 발생 시, login 성공 후 redirect 방식은 Request Cache 로 동작하므로 Redirect URL 을 따로 저장하지 않는다.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logging(request);
        response.sendRedirect("/login");
    }

    private static void logging(HttpServletRequest request) {
        log.info("CustomAuthEntryPoint.commence");
        log.info("referer = {}", request.getHeader("Referer"));
        log.info("requestURI = {}", request.getRequestURI());
    }

}