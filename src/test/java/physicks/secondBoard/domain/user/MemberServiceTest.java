package physicks.secondBoard.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
class MemberServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void PasswordEncoding() {
        String encodedHello = passwordEncoder.encode("hello");
        // $2a$10$RLXt65DgstImwI8H8Rk98ehXcLYtNWtXlKPa9/5yBQ42fbWwhBpXC
        // $2a$10$rLUNcAUDHdT9rZJppfUQS.Br0IkCMYbDgfbK1KirBMy7uiVfpPH6m

        log.info("encodedHello = {}", encodedHello);
    }

}