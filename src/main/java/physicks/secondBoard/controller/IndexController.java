package physicks.secondBoard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String index(Authentication authentication) {
        log.info("auth username : {}", authentication != null ? authentication : "NULL");
        return "index";
    }
}
