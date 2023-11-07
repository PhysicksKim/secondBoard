package physicks.secondBoard.domain.token;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PostUpdateTokenService {

    private final String SECRET_KEY; // 환경 변수로 주입 받음
    private static final long EXPIRATION_TIME = 86400000; // 86400000 ms = 24 hr

    public PostUpdateTokenService(@Value("${secret.key:#{null}}") String secretKey) {
        if (secretKey == null) {
            throw new IllegalArgumentException("환경 변수 'SECRET_KEY'가 설정되어야 합니다.");
        }
        this.SECRET_KEY = secretKey;
    }

    @PostConstruct
    public void init() {
        if (this.SECRET_KEY.isEmpty()) {
            throw new IllegalArgumentException("환경 변수 'SECRET_KEY'는 비어 있으면 안 됩니다.");
        }
    }

    public String generateUpdateAccessToken(long postId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("postId", postId);

        Date issuedDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        return Jwts.builder()
                .claims(claims)
                .subject("post-edit")
                .issuedAt(issuedDate)
                .expiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateUpdateAccessToken(String token, long postId) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token);

            Long tokenPostId = claims.getBody().get("postId", Long.class);
            return tokenPostId != null && tokenPostId.equals(postId) && !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation error", e);
            return false;
        }
    }
}
