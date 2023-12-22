package physicks.secondBoard.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @DisplayName("패스워드를 인코딩 하고 검증합니다")
    @Test
    void Basic_PasswordEncoder_Test() {
        String inputStr1 = "hello";
        String inputStr2 = "hello";
        log.info("inputStr1 = {}", inputStr1);
        log.info("inputStr2 = {}", inputStr2);

        String encode1 = passwordEncoder.encode(inputStr1);
        String encode2 = passwordEncoder.encode(inputStr2);
        log.info("encode1 : {}", encode1);
        log.info("encode2 : {}", encode2);

        boolean matches1 = passwordEncoder.matches(inputStr1, encode1);
        boolean matches2 = passwordEncoder.matches(inputStr2, encode2);
        log.info("matches1 : {}", matches1);
        log.info("matches2 : {}", matches2);
    }
}
