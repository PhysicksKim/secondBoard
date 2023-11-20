package physicks.secondBoard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(("/api"))
@Slf4j
public class TestApiController {

    @GetMapping("/test")
    @ResponseBody
    public String test(Authentication authentication) {
        log.info("authentication : {}", authentication);
        return "test";
    }

}
