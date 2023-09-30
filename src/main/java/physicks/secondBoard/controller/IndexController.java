package physicks.secondBoard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        // SessionUser user = (SessionUser) httpSession.getAttribute("user");
        //
        // if (user != null) {
        //     model.addAttribute("username", user.getName());
        // } else {
        //     model.addAttribute("username", null);
        // }

        return "index";
    }
}
