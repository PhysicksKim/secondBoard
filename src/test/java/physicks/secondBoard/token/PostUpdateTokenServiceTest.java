package physicks.secondBoard.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import physicks.secondBoard.domain.board.dto.PostGuestWriteDto;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostService;
import physicks.secondBoard.domain.token.PostUpdateTokenService;

@Slf4j
@SpringBootTest
public class PostUpdateTokenServiceTest {

    @Autowired
    private PostUpdateTokenService postUpdateTokenService;

    @Autowired
    private PostService postService;

    private Long savedPostId = -1L;

    @BeforeEach
    public void samplePost() {
        PostGuestWriteDto dto = new PostGuestWriteDto("testTitle", "guest1","Password1!","hello this is test post");
        Post post = postService.savePost(dto);
        savedPostId = post.getId();
        log.info("test post add : {}", post);
        log.info("savedPostId : {}", savedPostId);
    }

    @Test
    @DisplayName("기본 토큰 생성 및 유효성 검사")
    void token_create_validation() {
        // given
        String token = postUpdateTokenService.generateUpdateAccessToken(savedPostId);

        // when && then
        boolean isValidToken = postUpdateTokenService.validateUpdateAccessToken(token, savedPostId);

        Assertions.assertTrue(isValidToken, "토큰 유효성 검사");
    }
}
