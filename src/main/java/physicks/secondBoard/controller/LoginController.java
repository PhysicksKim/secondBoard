package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import physicks.secondBoard.domain.member.MemberLoginDto;
import physicks.secondBoard.domain.member.MemberService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("memberLoginDto", new MemberLoginDto());

        return "pages/login/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginDto memberLoginDto) {


        return "redirect:/";
    }


}
