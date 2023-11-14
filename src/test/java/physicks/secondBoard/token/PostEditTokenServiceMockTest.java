package physicks.secondBoard.token;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import physicks.secondBoard.domain.token.HS256KeyProvider;
import physicks.secondBoard.domain.token.PostEditTokenService;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
public class PostEditTokenServiceMockTest {

    @Mock
    private HS256KeyProvider keyProvider;

    @InjectMocks
    private PostEditTokenService tokenService;

    private SecretKey secretKey;
    private long postId = 123L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        secretKey = Jwts.SIG.HS256.key().build();
        when(keyProvider.getSECRET_KEY()).thenReturn(secretKey);
        tokenService = new PostEditTokenService(keyProvider);
    }

    @Test
    void generateAndValidateEditAccessToken() {
        String accessToken = tokenService.generateEditAccessToken(postId);
        assertNotNull(accessToken);

        boolean isValid = tokenService.validateEditAccessToken(accessToken, postId);
        assertTrue(isValid);
    }

    @Test
    void generateAndValidateEditRefreshToken() {
        String refreshToken = tokenService.generateEditRefreshToken(postId);
        assertNotNull(refreshToken);

        boolean isValid = tokenService.validateEditRefreshToken(refreshToken, postId);
        assertTrue(isValid);
    }

    @Test
    void validateExpiredToken() {
        String expiredToken = Jwts.builder()
                .subject("expiredToken")
                .expiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(secretKey)
                .compact();

        assertFalse(tokenService.validateEditAccessToken(expiredToken, postId));
        assertFalse(tokenService.validateEditRefreshToken(expiredToken, postId));
    }

    @Test
    void validateTokenWithWrongPostId() {
        String token = tokenService.generateEditAccessToken(postId);
        assertFalse(tokenService.validateEditAccessToken(token, 999L));
    }

    @Test
    void validateInvalidToken() {
        String invalidToken = "invalidToken";
        assertFalse(tokenService.validateEditAccessToken(invalidToken, postId));
        assertFalse(tokenService.validateEditRefreshToken(invalidToken, postId));
    }
}
