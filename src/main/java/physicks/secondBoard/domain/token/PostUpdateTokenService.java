package physicks.secondBoard.domain.token;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * HS256 사용
 */
@Slf4j
@Service
public class PostUpdateTokenService {

    private static final String CLAIM_POST_ID = "postId";
    private static final String SUBJECT = "postUpdate";
    private static final long EXPIRATION_TIME = 86400000; // 86400000 ms = 24 hr

    private final SecretKey SECRET_KEY; // provider 에 의해 주입받음

    public PostUpdateTokenService(HS256KeyProvider provider) {
        SECRET_KEY = provider.getSECRET_KEY();
    }

    public String generateUpdateAccessToken(long postId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_POST_ID, postId);

        Date issuedDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        return Jwts.builder()
                .claims(claims)
                .subject(SUBJECT)
                .issuedAt(issuedDate)
                .expiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateUpdateAccessToken(String token, long postId) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
            Long tokenPostId = claimsJws.getPayload().get(CLAIM_POST_ID, Long.class);
            return tokenPostId != null && tokenPostId.equals(postId) && claimsJws.getPayload().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation error", e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected Token error", e);
            return false;
        }
    }
}
