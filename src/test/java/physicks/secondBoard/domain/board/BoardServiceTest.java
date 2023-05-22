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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

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

    @Test
    public void TestUnitMethod_getBoardPostList() throws Exception {
        //given
        // Post 데이터를 생성
        Post post1 = new Post("title1", "author1", "content1");
        Post post2 = new Post("title2", "author2", "content2");
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

    // @Test
    // public void findPostById() throws Exception {
    //     //given
    //
    //     //when
    //
    //     //then
    // }
    //
    // @Test
    // public void savePost() throws Exception {
    //     //given
    //
    //     //when
    //
    //     //then
    // }

    @Test
    public void post_saveAndFind() throws Exception {
        //given
        Post post = new Post("title", "author", "content");

        //when
        Post savedPost = boardService.savePost(post);
        Post foundPost = boardService.findPostById(savedPost.getId());

        //then
        assertThat(savedPost).isNotNull();
        assertThat(foundPost).isNotNull();

        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(foundPost.getAuthor()).isEqualTo(post.getAuthor());
        assertThat(foundPost.getContent()).isEqualTo(post.getContent());
    }

}