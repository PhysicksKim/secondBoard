package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import physicks.secondBoard.domain.signup.SignupForm;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupController {

    private static final String VIEW_PREFIX = "pages/signup/";

    @GetMapping("/signup")
    public String register() {
        return VIEW_PREFIX + "signup";
    }

    @PostMapping("/signup")
    @ResponseBody
    public String singupPost(SignupForm signupForm) throws IOException {
        log.info("dto : {}", signupForm);
        return "GOOD";
    }
}