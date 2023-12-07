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
public class PostEditTokenService {

    private static final String CLAIM_KEY_POST_ID = "postId";
    private static final String CLAIM_KEY_TOKEN_TYPE = "type";
    private static final String CLAIM_VALUE_TYPE_ACCESS = "access";
    private static final String CLAIM_VALUE_TYPE_REFRESH = "refresh";
    private static final String SUBJECT_FOR_ACCESS_TOKEN = "postUpdate";
    private static final String SUBJECT_FOR_REFRESH_TOKEN = "postUpdateRefresh";
    private static final long ACCESS_TOKEN_EXPIRATION_MS = 86400000; // 86400000 ms = 24 hr
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 86400000; // 86400000 ms = 24 hr

    private final SecretKey SECRET_KEY; // provider 에 의해 주입받음

    public PostEditTokenService(HS256KeyProvider provider) {
        SECRET_KEY = provider.getSECRET_KEY();
    }

    public String generateEditAccessToken(long postId) {
        return generateToken(postId, CLAIM_VALUE_TYPE_ACCESS, ACCESS_TOKEN_EXPIRATION_MS, SUBJECT_FOR_ACCESS_TOKEN);
    }

    public String generateEditRefreshToken(long postId) {
        return generateToken(postId, CLAIM_VALUE_TYPE_REFRESH, REFRESH_TOKEN_EXPIRATION_MS, SUBJECT_FOR_REFRESH_TOKEN);
    }

    /**
     * 토큰이 유효하지 않은 경우 false 를 반환합니다.
     * 토큰이 유효하지 않으면 추가적인 작업을 상위 service 에서 처리해야 하므로, 예외 대신 boolean 을 반환하도록 합니다.
     * @param token
     * @param postId
     */
    public boolean validateEditAccessToken(String token, long postId) {
        try {
            Jws<Claims> claimsJws = getClaimsFromToken(token);
            return isAccessToken(claimsJws) && isValidTokenPostId(claimsJws, postId) && isNotExpired(claimsJws);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation error", e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected Token error", e);
            return false;
        }
    }

    public boolean validateEditRefreshToken(String token, long postId) {
        try {
            Jws<Claims> claimsJws = getClaimsFromToken(token);
            return isRefreshToken(claimsJws) && isValidTokenPostId(claimsJws, postId) && isNotExpired(claimsJws);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation error", e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected Token error", e);
            return false;
        }
    }

    private String generateToken(long postId, String claimTokenTypeValueRefresh, long refreshTokenExpirationTime, String subjectRefresh) {
        Map<String, Object> claims = getPostIdClaim(postId);
        claims.put(CLAIM_KEY_TOKEN_TYPE, claimTokenTypeValueRefresh);

        Date issuedDate = getIssuedDate();
        Date expirationDate = getExpirationDate(refreshTokenExpirationTime);

        return Jwts.builder()
                .claims(claims)
                .subject(subjectRefresh)
                .issuedAt(issuedDate)
                .expiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    private Jws<Claims> getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
    }

    private boolean isAccessToken(Jws<Claims> claimsJws) {
        String tokenType = claimsJws.getPayload().get(CLAIM_KEY_TOKEN_TYPE, String.class);
        return tokenType != null && tokenType.equals(CLAIM_VALUE_TYPE_ACCESS);
    }

    private boolean isRefreshToken(Jws<Claims> claimsJws) {
        String tokenType = claimsJws.getPayload().get(CLAIM_KEY_TOKEN_TYPE, String.class);
        return tokenType != null && tokenType.equals(CLAIM_VALUE_TYPE_REFRESH);
    }

    private boolean isValidTokenPostId(Jws<Claims> claimsJws, long postId) {
        Long tokenPostId = claimsJws.getPayload().get(CLAIM_KEY_POST_ID, Long.class);
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
        claims.put(CLAIM_KEY_POST_ID, postId);
        return claims;
    }

    private Date getIssuedDate() {
        return new Date(System.currentTimeMillis());
    }
}
