package physicks.secondBoard.domain.board;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.board.dto.PostGuestWriteDto;
import physicks.secondBoard.domain.board.service.BoardService;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostService;
import physicks.secondBoard.domain.token.PostEditTokenService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * <pre>
 * BoardService 의 메서드들을 테스트 합니다.
 * mock 대상 : PostRepository
 * BoardService 는 PostRepository 주입을 필요로 하므로 mock을 생성해 setup()에서 Mock을 주입해줍니다.
 *
 * 본 테스트는 BoardService 에 대한 테스트 입니다.
 * 따라서 Post 객체 일치여부는 모든 필드에 대해 비교하지 않습니다.
 * Post Mapping 비교는 아래 테스트에서 수행합니다.
 * </pre>
 * @see PostListDtoMapperTest
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
@Deprecated
class BoardServiceMockTest {

    @Mock
    private PostService postService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PostEditTokenService postEditTokenService;
    private BoardService boardService;
    private static final String POST_ID_FIELD = "id";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
        boardService = new BoardService(postService, postEditTokenService, passwordEncoder); // 테스트할 Service 에 Mock 객체를 주입
    }

    /**
     * 너무 많은 MOCK 이 필요해지므로 본 테스트는 삭제. 다만 기록을 위해 메서드 흔적만 남겨둠.
     */
    // @Test
    @Deprecated
    public void getBoardPostList() throws Exception {
        // 너무 많은 Mocking 이 필요해짐.
        // postRepository와 boardPostListDtoMapper에 대해서 메서드 mock이 필요해지므로
        // 테스트의 대부분이 mock으로만 구성되는 결과로 이어짐.
    }

    /**
     * boardService 의 findPostById() 메서드를 mock 테스트 합니다 <br>
     */
    @Test
    public void findPostById() throws Exception {
        //given
        Author author1 = Author.ofGuest("author1", "password1");
        Author author2 = Author.ofGuest("author2", "password2");

        Post post1 = Post.of("title1", author1, "content1");
        Post post2 = Post.of("title2", author2, "content2");
        ReflectionTestUtils.setField(post1,POST_ID_FIELD, 1L);
        ReflectionTestUtils.setField(post2,POST_ID_FIELD, 2L);

        // Mock 주입
        when(postService.findPostById(1L))
                .thenReturn(post1);
        when(postService.findPostById(2L))
                .thenReturn(post2);

        //when
        Post findPost_id1L = boardService.findPostById(1L);
        Post findPost_id2L = boardService.findPostById(2L);

        //then
        verify(postService, times(1)).findPostById(1L);
        verify(postService, times(1)).findPostById(2L);
        assertThat(post1.getId()).isEqualTo(findPost_id1L.getId());
        assertThat(post2.getId()).isEqualTo(findPost_id2L.getId());
        assertPostEqual(post1, findPost_id1L);
        assertPostEqual(post2, findPost_id2L);
    }

    /**
     * Post의 title, author, content 필드가 같은지를 assert 합니다.
     */
    private void assertPostEqual(Post original, Post find) {
        assertThat(original.getTitle()).isEqualTo(find.getTitle());
        assertThat(original.getAuthor()).isEqualTo(find.getAuthor());
        assertThat(original.getContent()).isEqualTo(find.getContent());
    }

    @Test
    public void savePost() throws Exception {
        //given
        PostGuestWriteDto postDto = new PostGuestWriteDto("title1", "author1", "password1", "content1");
        Post post1 = Post.of("title1", Author.ofGuest("author1", "password1"), "content1");

        when(postService.savePost(postDto)).thenReturn(post1);

        //when
        Post savedPost = boardService.savePost(postDto);

        //then
        verify(postService, times(1)).savePost(postDto);
        assertThat(savedPost.getTitle()).isEqualTo(post1.getTitle());
        assertThat(savedPost.getAuthor()).isEqualTo(post1.getAuthor());
        assertThat(savedPost.getContent()).isEqualTo(post1.getContent());
    }
}