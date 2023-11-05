package physicks.secondBoard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    public String index(Model model, Authentication authentication) {
        log.debug("auth username : {}", authentication != null ? authentication : "NULL");
        return "index";
    }
}
