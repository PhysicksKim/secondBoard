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
    private static final String CLAIM_TOKEN_TYPE_KEY = "type";
    private static final String CLAIM_TOKEN_TYPE_VALUE_ACCESS = "access";
    private static final String CLAIM_TOKEN_TYPE_VALUE_REFRESH = "refresh";
    private static final String SUBJECT_ACCESS = "postUpdate";
    private static final String SUBJECT_REFRESH = "postUpdateRefresh";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 86400000; // 86400000 ms = 24 hr
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 86400000; // 86400000 ms = 24 hr

    private final SecretKey SECRET_KEY; // provider 에 의해 주입받음

    public PostUpdateTokenService(HS256KeyProvider provider) {
        SECRET_KEY = provider.getSECRET_KEY();
    }

    public String generateUpdateAccessToken(long postId) {
        Map<String, Object> claims = getPostIdClaim(postId);
        claims.put(CLAIM_TOKEN_TYPE_KEY, CLAIM_TOKEN_TYPE_VALUE_ACCESS);

        Date issuedDate = getIssuedDate();
        Date expirationDate = getExpirationDate(ACCESS_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .claims(claims)
                .subject(SUBJECT_ACCESS)
                .issuedAt(issuedDate)
                .expiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateUpdateRefreshToken(long postId) {
        Map<String, Object> claims = getPostIdClaim(postId);
        claims.put(CLAIM_TOKEN_TYPE_KEY, CLAIM_TOKEN_TYPE_VALUE_REFRESH);

        Date issuedDate = getIssuedDate();
        Date expirationDate = getExpirationDate(REFRESH_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .claims(claims)
                .subject(SUBJECT_REFRESH)
                .issuedAt(issuedDate)
                .expiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateUpdateAccessToken(String token, long postId) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
            return isValidTokenPostId(claimsJws, postId) && isNotExpired(claimsJws) && isAccessToken(claimsJws);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation error", e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected Token error", e);
            return false;
        }
    }

    public boolean validateUpdateRefreshToken(String token, long postId) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
            return isValidTokenPostId(claimsJws, postId) && isNotExpired(claimsJws) && isRefreshToken(claimsJws);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation error", e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected Token error", e);
            return false;
        }
    }

    private boolean isAccessToken(Jws<Claims> claimsJws) {
        String tokenType = claimsJws.getPayload().get(CLAIM_TOKEN_TYPE_KEY, String.class);
        return tokenType != null && tokenType.equals(CLAIM_TOKEN_TYPE_VALUE_ACCESS);
    }

    private boolean isRefreshToken(Jws<Claims> claimsJws) {
        String tokenType = claimsJws.getPayload().get(CLAIM_TOKEN_TYPE_KEY, String.class);
        return tokenType != null && tokenType.equals(CLAIM_TOKEN_TYPE_VALUE_REFRESH);
    }

    private boolean isValidTokenPostId(Jws<Claims> claimsJws, long postId) {
        Long tokenPostId = claimsJws.getPayload().get(CLAIM_POST_ID, Long.class);
        return tokenPostId != null && tokenPostId.equals(postId);
    }

    private boolean isNotExpired(Jws<Claims> claimsJws) {
        return claimsJws.getPayload().getExpiration().after(new Date());
    }

    private Date getExpirationDate(long expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime);
    }

    private Map<String, Object> getPostIdClaim(long postId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_POST_ID, postId);
        return claims;
    }

    private Date getIssuedDate() {
        return new Date(System.currentTimeMillis());
    }
}
