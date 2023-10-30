package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import physicks.secondBoard.domain.member.signup.SignupForm;

import javax.validation.Valid;
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
    public String singupPost(@Valid SignupForm signupForm, BindingResult bindingResult) throws IOException {
        log.info("dto : {}", signupForm);
        // dto : SignupForm(
        // email=test@xxx.com,
        // password=123456,
        // passwordCheck=12341235123,
        // username=kim,
        // check=true)

        if (signupForm.getPassword() != signupForm.getPasswordCheck()) {
            bindingResult.rejectValue("passwordCheck", "PasswordCheckMismatch");
        }
        if(signupForm.isCheck() == false) {
            bindingResult.rejectValue("isCheck", "NotAgree");
        }
        if(bindingResult.hasErrors()) {
            // 에러 결과 반환
        }

        // 미완성

        return "GOOD";
    }
}