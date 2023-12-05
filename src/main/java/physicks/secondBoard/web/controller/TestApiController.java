package physicks.secondBoard.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class TestApiController {

    @GetMapping("/api/test")
    @ResponseBody
    public String test(Authentication authentication) {
        log.info("authentication : {}", authentication);
        return "test";
    }

    @GetMapping("/links")
    public String apiPage() {
        return "pages/testPage/api";
    }

}
