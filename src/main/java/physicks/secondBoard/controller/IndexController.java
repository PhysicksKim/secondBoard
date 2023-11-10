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
        // 비회원인 경우 AnonymousAuthenticationToken 이 아니라 null 이 들어온다.
        log.info("auth username : {}", authentication != null ? authentication : "NULL");
        return "index";
    }
}
