package physicks.secondBoard.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * thymeleaf 가 로그인한 사용자의 username 을 얻을 수 있도록
 * model 에 attributeName : "username" 으로 유저 이름을 담아준다.
 * 만약 user 가 세션에 없는 경우에는
 * thymeleaf 가 user 정보를 쓸 수 없으므로
 * "username" attribute 에 null 을 담아주며
 * thymeleaf 에서는 null 체크로 user 가 있는지 없는지 판단한다.
 * </pre>
 */
@Slf4j
@Component
public class UsernameAddInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!isUsernameRequireView(modelAndView)) {
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!isLoginUser(authentication)) {
            modelAndView.addObject("username", null);
            return;
        }

        log.debug("authentication instanceof OAuth2AuthenticationToken == {}", authentication instanceof OAuth2AuthenticationToken);
        if(authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            printDebugLog(authentication);
            OAuth2User principal = oauthToken.getPrincipal();
            modelAndView.addObject("username", principal.getAttributes().get("name"));
        } else {
            modelAndView.addObject("username", authentication.getName());
        }
    }

    private boolean isLoginUser(Authentication authentication) {
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    private boolean isUsernameRequireView(ModelAndView modelAndView) {
        return modelAndView != null && !isRedirectView(modelAndView) && modelAndView.getModelMap().getAttribute("username") == null;
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();
        return viewName != null && viewName.startsWith("redirect:");
    }

    private void printDebugLog(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = oauthToken.getPrincipal();
        log.debug("OAuth2 User Intercept :: registrationId = {}", oauthToken.getAuthorizedClientRegistrationId());
        log.debug("OAuth2 User Intercept :: principal = {}", principal);
        log.debug("OAuth2 User Intercept :: principal.getAttributes().get(\"name\") = {}", principal.getAttributes().get("name")); // 사용자 닉네임
        log.debug("OAuth2 User Intercept :: principal.getName() = {}", principal.getName()); // oauth 식별자. Long 형식 ID (ex. 1142082040193305)
    }
}
