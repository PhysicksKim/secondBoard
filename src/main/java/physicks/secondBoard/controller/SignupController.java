package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import physicks.secondBoard.domain.member.signup.SignupForm;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupController {

    private static final String VIEW_PREFIX = "pages/signup/";

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return VIEW_PREFIX + "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute @Valid SignupForm signupForm, BindingResult bindingResult) throws IOException {
        log.info("dto : {}", signupForm);

        // validation
        if (signupForm.getPassword() != null &&
                !signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
            log.info("password is not equal!! :: password : {} , passwordCheck : {}", signupForm.getPassword(), signupForm.getPasswordCheck());
            bindingResult.rejectValue("passwordCheck", "PasswordCheckMismatch", "패스워드 확인이 패스워드와 다릅니다");
        }
        if (signupForm.isCheck() == false) {
            bindingResult.rejectValue("isCheck", "NotAgree", "약관에 동의해 주세요");
        }

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            int errCount = bindingResult.getErrorCount();
            sb.append("error count {").append(errCount).append("}\n");

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                sb.append("Field: ").append(fieldName).append(", Error: ").append(errorMessage).append('\n');
            }
            log.info("signup controller invalid 발생 :: {}", sb);

            return VIEW_PREFIX + "signup";
        }

        return "redirect:/";
    }
}