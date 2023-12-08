package physicks.secondBoard.domain.token;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class BasicJwtTest {

    @Test
    void jwtTest() {
        // 비밀 키 생성
        Key key = Jwts.SIG.HS256.key().build();
        log.info("key :: format = {} , encoded = {} , algorithm = {}", key.getFormat(), key.getEncoded(), key.getAlgorithm());
        String base64EncodedKey = Base64.getEncoder().encodeToString(key.getEncoded()); // KOH2mCHF5T4lQA1sHCs0yaESHptMw4lvBR6mge5ssqU=
        log.info("Base64 Encoded Key: {}", base64EncodedKey);

        // 현재 시각
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 토큰 만료 시각 설정 (예: 현재로부터 1시간 후)
        long expMillis = nowMillis + 3600000;
        Date exp = new Date(expMillis);

        String compact = Jwts.builder()
                .header()
                .keyId("testKeyId")
                .and()
                .subject("testBasic")
                .signWith(key)
                .compact();

        log.info("jwt key : {}", compact);
    }
}
