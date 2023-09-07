package physicks.secondBoard.domain.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.board.dto.BoardPostDto;
import physicks.secondBoard.domain.board.dto.PostGuestWriteDto;
import physicks.secondBoard.domain.board.service.BoardService;
import physicks.secondBoard.domain.post.Post;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BoardServiceSpringTest {

    @Autowired
    private BoardService boardService;

    @PersistenceContext
    private EntityManager em;

    private final int postNums = 13;
    private final int size = 5;

    @BeforeEach
    public void setup() {
        for(int i = 1 ; i <= postNums ; i++) {
            PostGuestWriteDto postGuestWriteDto = new PostGuestWriteDto("title" + i, "author" + i, "password" + i, "content" + i);
            boardService.savePost(postGuestWriteDto);
        }
    }

    @Test
    public void post_saveAndFind() throws Exception {
        //given
        PostGuestWriteDto postGuestWriteDto = new PostGuestWriteDto("title","author","password", "content");

        //when
        Post savedPost = boardService.savePost(postGuestWriteDto);
        em.flush();
        em.clear();

        Post foundPost = boardService.findPostById(savedPost.getId());

        //then
        assertThat(savedPost).isNotNull();
        assertThat(foundPost).isNotNull();

        assertThat(foundPost.getId()).isEqualTo(savedPost.getId());
        assertThat(foundPost.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(foundPost.getAuthor()).isEqualTo(savedPost.getAuthor());
        assertThat(foundPost.getContent()).isEqualTo(savedPost.getContent());
    }

    /**
     * Pagination 테스트 1 <br>
     * 최신 게시글 페이지 조회 테스트 <br>
     */
    @Test
    public void getBoardPostList_PaginationTest_NewestPage() throws Exception{
        //given
        final int page = 0;
        Pageable pageable = PageRequest.of(page, size);

        //when
        List<BoardPostDto> dtoList = boardService.getBoardPostList(pageable);

        //then
        assertThat(dtoList.size()).isEqualTo(size);
        for(int i = 0 ; i < size ; i++) {
            assertThat(dtoList.get(i).getTitle()).isEqualTo("title"+(postNums-i));
        }
    }

    // Pagination 테스트 2. 중간 페이지 조회 (ex. 총 3페이지 중 2페이지 조회)
    @Test
    public void getBoardPostList_PaginationTest_MiddlePage() throws Exception{
        //given
        final int page = 1;
        Pageable pageable = PageRequest.of(page, size);

        //when
        List<BoardPostDto> dtoList = boardService.getBoardPostList(pageable);

        //then
        assertThat(dtoList.size()).isEqualTo(size);
        for(int i = 0 ; i < size ; i++) {
            assertThat(dtoList.get(i).getTitle()).isEqualTo("title"+(postNums-i-size*page));
        }
    }


    // Pagination 테스트 3. 마지막 페이지 조회 (ex. 총 3페이지 중 3페이지 조회)
    @Test
    public void getBoardPostList_PaginationTest_OldestPage() throws Exception{
        //given
        final int page = 2;
        final int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        //when
        List<BoardPostDto> dtoList = boardService.getBoardPostList(pageable);
        for (BoardPostDto boardPostDto : dtoList) {
            System.out.println("dto title = " + boardPostDto.getTitle());
        }
        List<Post> all = boardService.findAll();
        for (Post post : all) {
            System.out.println("post.getTitle() = " + post.getTitle());
        }

        //then
        assertThat(dtoList.size()).isEqualTo(3);
        for(int i = 0 ; i < postNums%size ; i++) {
            assertThat(dtoList.get(i).getTitle()).isEqualTo("title"+(postNums-i-size*page));
        }
    }

    @Test
    public void postUpdateTest() throws Exception {
        // given
        List<BoardPostDto> boardPostList = boardService.getBoardPostList(Pageable.ofSize(1));
        Long id = boardPostList.get(0).getId();
        Post findPost = boardService.findPostById(id);

        // when
        String title = "Updated Title";
        String content = "Updated Content";
        String authorName = "GuestAuthor";

        findPost.updateTitleAndContent(title, content);
        findPost.updateAuthor(authorName);

        em.flush();
        em.clear();

        // then
        Post updatedPost = boardService.findPostById(id);
        assertThat(updatedPost.getTitle()).isEqualTo(findPost.getTitle());
        assertThat(updatedPost.getAuthor().getAuthorName()).isEqualTo(findPost.getAuthor().getAuthorName());
        assertThat(updatedPost.getTitle()).isEqualTo(findPost.getTitle());
    }
}
