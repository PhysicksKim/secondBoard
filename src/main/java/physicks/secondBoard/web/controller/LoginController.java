package physicks.secondBoard.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import physicks.secondBoard.config.SecurityConstants;

// TODO : Redirect 전체 과정과 case 에 대해 정리해야 한다. 그림 그려서 전체 flow 보고, test 어떻게 할지 생각해보자.
/**
 * 로그인 페이지를 보여주는 컨트롤러입니다.
 * 로그인 처리는 Spring Security 가 담당합니다.
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    /**
     * Form Request 에는 쿼리 파라미터를 포함할 수 없으므로, form 에 hidden 값으로 redirectUrl 을 추가합니다.
     * redirect 동작은 CustomAuthenticationSuccessHandler 에서 처리합니다.
     * @param model
     * @param redirectUrl
     * @return
     */
    @GetMapping("/login")
    public String showLoginPage(Model model,
                            @RequestParam(value = "redirect", required = false) String redirectUrl) {

        // Redirect URL 이 존재하는 경우, form 에 hidden 값으로 redirectUrl 을 추가합니다
        model.addAttribute("redirectParamKey", SecurityConstants.LOGIN_REDIRECT_PARAM);
        model.addAttribute("redirectUrl", redirectUrl);

        return "pages/login/login";
    }

}
