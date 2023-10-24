package physicks.secondBoard.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import physicks.secondBoard.domain.oauth.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
@Component
public class UserSessionInterceptor implements HandlerInterceptor {

    @Autowired
    private HttpSession httpSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // view 를 return 하지 않는 경우에는 ModelAndView 가 null 일 수 있으므로 단락회로 평가 필요 (ex. REST API)
        if (modelAndView != null && request.getMethod().equals("GET")) {
            SessionUser user = (SessionUser) httpSession.getAttribute("user");
            if (user != null) {
                modelAndView.addObject("username", user.getName());
            } else {
                modelAndView.addObject("username", null);
            }
        }
    }
}
