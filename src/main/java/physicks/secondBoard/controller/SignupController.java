package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import physicks.secondBoard.domain.user.MemberService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupController {

    private static final String VIEW_PREFIX = "pages/signup/";

    @GetMapping("/signup")
    public String register() {
        return VIEW_PREFIX + "signup";
    }
}
