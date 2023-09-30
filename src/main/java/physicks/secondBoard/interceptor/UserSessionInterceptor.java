package physicks.secondBoard.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import physicks.secondBoard.config.oauth.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
