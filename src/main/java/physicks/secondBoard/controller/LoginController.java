package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import physicks.secondBoard.config.SecurityConstants;
import physicks.secondBoard.domain.member.MemberLoginDto;
import physicks.secondBoard.domain.member.MemberService;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage(Model model,
                            HttpServletRequest request,
                            @RequestParam(value = "redirect", required = false) String redirectUrl) {
        model.addAttribute("memberLoginDto", new MemberLoginDto());
        model.addAttribute("redirectParamKey", SecurityConstants.LOGIN_REDIRECT_PARAM);
        model.addAttribute("redirectUrl", redirectUrl);

        String referer = request.getHeader("Referer");
        log.info("login page :: referer = {}", referer);
        log.info("login page :: requestURI = {}", request.getRequestURI());

        return "pages/login/login";
    }

}
