package physicks.secondBoard.domain.board;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * BoardService 의 메서드들을 테스트 합니다.  <br>
 * mock 대상 : PostRepository <br>
 * BoardService 는 PostRepository 주입을 필요로 하므로 mock을 생성해 setup()에서 Mock을 주입해줍니다.  <br>
 * <br>
 * 본 테스트는 BoardService 에 대한 테스트 입니다. <br>
 * 따라서 Post 객체 일치여부는 모든 필드에 대해 비교하지 않습니다. <br>
 * Post Mapping 비교는 아래 테스트에서 수행합니다 <br>
 * @see BoardPostListDtoMapperTest
 */
// @SpringBootTest // @SpringBootTest 를 사용하면 빈과 mock이 충돌해서 문제된다
@Slf4j
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private PostRepository postRepository;

    // @InjectMocks
    private BoardService boardService;

    private static final String POST_ID_FIELD = "id";
    private static final String POST_CREATED_TIME_FIELD = "createdTime";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
        boardService = new BoardService(postRepository); // 테스트할 Service에 Mock 객체를 주입
    }

    /**
     * boardService 의 getBoardPostList() 메서드를 테스트 합니다 <br>
     * Pagination 테스트는 BoardServiceSpringTest 파일의 getBoardPostList_PaginationTest_ 에서 다룹니다. <br>
     * <br>
     * {@link BoardServiceSpringTest#getBoardPostList_PaginationTest_NewestPage}
     * {@link BoardServiceSpringTest#getBoardPostList_PaginationTest_MiddlePage}
     * {@link BoardServiceSpringTest#getBoardPostList_PaginationTest_OldestPage}
     */
    @Test
    public void getBoardPostList() throws Exception {
        //given
        // Post 데이터를 생성
        Post post1 = Post.of("title1", "author1", "content1");
        Post post2 = Post.of("title2", "author2", "content2");
        ReflectionTestUtils.setField(post1,POST_ID_FIELD, 1L);
        ReflectionTestUtils.setField(post2,POST_ID_FIELD, 2L);
        ReflectionTestUtils.setField(post1,POST_CREATED_TIME_FIELD, LocalDateTime.now());
        ReflectionTestUtils.setField(post2,POST_CREATED_TIME_FIELD, LocalDateTime.now());

        // Pageable 생성
        Pageable pageable = PageRequest.of(0, 2);

        // Mock 주입
        when(postRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(new PageImpl<>(Arrays.asList(post1, post2)));

        //when
        List<BoardPostListDto> postList = boardService.getBoardPostList(pageable);

        //then
        assertThat(postList).isNotNull()
                .withFailMessage("postList가 null이면 안됨");
        assertThat(postList.size()).isEqualTo(2)
                .withFailMessage("postList size는 2여야 합니다");

        assertThat(postList.get(0).getId()).isEqualTo(post1.getId())
                .withFailMessage("첫번째 post.id가 given의 post1.id와 다릅니다");
        assertThat(postList.get(1).getId()).isEqualTo(post2.getId())
                .withFailMessage("두번째 post.id가 given의 post2.id과 다릅니다");
    }



    /**
     * boardService 의 findPostById() 메서드를 테스트 합니다 <br>
     */
    @Test
    public void findPostById() throws Exception {
        //given
        Post post1 = Post.of("title1", "author1", "content1");
        Post post2 = Post.of("title2", "author2", "content2");
        ReflectionTestUtils.setField(post1,POST_ID_FIELD, 1L);
        ReflectionTestUtils.setField(post2,POST_ID_FIELD, 2L);

        // Mock 주입
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post1));
        when(postRepository.findById(2L))
                .thenReturn(Optional.of(post2));

        //when
        Post findPost_id1L = boardService.findPostById(1L);
        Post findPost_id2L = boardService.findPostById(2L);

        //then
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).findById(2L);
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
        Post post1 = Post.of("title1", "author1", "content1");

        when(postRepository.save(any(Post.class))).thenReturn(post1);

        //when
        Post savedPost = boardService.savePost(post1);

        //then
        verify(postRepository, times(1)).save(post1);
        assertThat(savedPost.getTitle()).isEqualTo(post1.getTitle());
        assertThat(savedPost.getAuthor()).isEqualTo(post1.getAuthor());
        assertThat(savedPost.getContent()).isEqualTo(post1.getContent());
    }
}