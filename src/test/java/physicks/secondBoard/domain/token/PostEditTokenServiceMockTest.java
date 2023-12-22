package physicks.secondBoard.domain.token;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @DisplayName("게시글 수정용 Access 토큰을 생성하고 검증합니다.")
    @Test
    void generateAndValidateEditAccessToken() {
        String accessToken = tokenService.generateEditAccessToken(postId);
        assertNotNull(accessToken);

        boolean isValid = tokenService.validateEditAccessToken(accessToken, postId);
        assertTrue(isValid);
    }

    @DisplayName("게시글 수정용 Refresh 토큰을 생성하고 검증합니다.")
    @Test
    void generateAndValidateEditRefreshToken() {
        String refreshToken = tokenService.generateEditRefreshToken(postId);
        assertNotNull(refreshToken);

        boolean isValid = tokenService.validateEditRefreshToken(refreshToken, postId);
        assertTrue(isValid);
    }

    @DisplayName("만료된 토큰은 false 를 반환합니다.")
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

    @DisplayName("게시글 수정용 Access 토큰의 게시글 ID 가 잘못되면 False 를 반환합니다.")
    @Test
    void validateTokenWithWrongPostId() {
        String token = tokenService.generateEditAccessToken(postId);
        assertFalse(tokenService.validateEditAccessToken(token, 999L));
    }

    @DisplayName("토큰 String 이 유효한 형식이 아니면 false 를 반환합니다.")
    @Test
    void validateInvalidToken() {
        String invalidToken = "invalidToken";
        assertFalse(tokenService.validateEditAccessToken(invalidToken, postId));
        assertFalse(tokenService.validateEditRefreshToken(invalidToken, postId));
    }
}
