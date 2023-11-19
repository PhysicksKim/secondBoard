package physicks.secondBoard.domain.member.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Form Login 에서 email 과 password 를 추출해서 Spring Security 의 login 절차를 수행한다.
 * @Deprecated
 * 기본 등록된 UsernamePasswordAuthenticationFilter 를 사용하면서 Username parameter 만 변경하면 되므로 CustomFormLoginFilter 는 Deprecated 되었습니다.
 * 로그인 과정에서 기본 UsernamePasswordAuthenticationFilter 를 사용하며, Username Parameter 만 email 로 설정해서 사용합니다.
 * SecurityConfig 에서 formLogin().usernameParameter("email") 으로 간단히 설정할 수 있습니다.
 * @see physicks.secondBoard.config.SecurityConfig
 */
@Deprecated
@Slf4j
public class CustomFormLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        log.info("CustomFormLoginFilter 커스텀 폼 로그인 과정 시작");
        setUsernameParameter("email"); // email 파라미터를 username 으로 사용

        String username = obtainUsername(request);
        username = (username != null) ? username.trim() : "";
        String password = obtainPassword(request);
        password = (password != null) ? password : "";

        // unauthorized token 생성. 이후 인증 매니저가 인증을 진행하고, 인증에 성공하면 Security Context 에 authorized token 이 저장된다.
        UsernamePasswordAuthenticationToken authRequest
                = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        // WebAuthenticationDetailsSource.buildDetails(request) 를 통해 details 를 생성한다.
        // details 에는 remoteAddress, sessionId 가 저장된다.
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
