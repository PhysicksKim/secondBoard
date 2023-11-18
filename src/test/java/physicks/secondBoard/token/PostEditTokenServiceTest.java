package physicks.secondBoard.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostService;
import physicks.secondBoard.domain.token.PostEditTokenService;

@Slf4j
@SpringBootTest
public class PostEditTokenServiceTest {

    @Autowired
    private PostEditTokenService postEditTokenService;

    @Autowired
    private PostService postService;

    private Long savedPostId = -1L;

    @BeforeEach
    public void samplePost() {
        Post post = postService.createPostOfGuest("testTitle", "guest1","Password1!","hello this is test post");
        savedPostId = post.getId();
        log.info("test post add : {}", post);
        log.info("savedPostId : {}", savedPostId);
    }

    @Test
    @DisplayName("Access Token 생성 및 유효성 검사")
    void access_token_create_validation() {
        // given
        String token = postEditTokenService.generateEditAccessToken(savedPostId);

        // when && then
        boolean isValidToken = postEditTokenService.validateEditAccessToken(token, savedPostId);

        Assertions.assertTrue(isValidToken, "토큰 유효성 검사");
    }
    @Test
    @DisplayName("Refresh Token 생성 및 유효성 검사")
    void refresh_token_create_validation() {
        // given
        String token = postEditTokenService.generateEditRefreshToken(savedPostId);

        // when && then
        boolean isValidToken = postEditTokenService.validateEditRefreshToken(token, savedPostId);

        Assertions.assertTrue(isValidToken, "토큰 유효성 검사");
    }
}
