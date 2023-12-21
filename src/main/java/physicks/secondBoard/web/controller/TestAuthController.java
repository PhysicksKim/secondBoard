package physicks.secondBoard.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class TestAuthController {

    @PostMapping("/test/auth")
    @ResponseBody
    public String testAuthPost(Authentication authentication) {
        // authentication 객체가 UsernamePasswordAuthenticationToken 인 경우와 null 인 경우에 대해서 테스트 해야 합니다.
        if(authentication != null) {
            log.info("NOT NULL 인 authentication : {}", authentication);
        } else {
            log.info("NULL 인 authentication : {}", authentication);
        }
        return "test-auth";
    }

}
