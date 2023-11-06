package physicks.secondBoard.jwt;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.Key;

@Slf4j
public class BasicJwtTest {

    @Test
    void jwtTest() {
        Key key = Jwts.SIG.HS256.key().build();

        String compact = Jwts.builder()
                .header()
                .keyId("testKeyId")
                .and()
                .subject("phy")
                .signWith(key)
                .compact();

        log.info("jwt key : {}", compact);
    }
}
