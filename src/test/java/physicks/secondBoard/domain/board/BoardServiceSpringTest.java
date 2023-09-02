package physicks.secondBoard.domain.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BoardServiceSpringTest {

    @Autowired
    private BoardService boardService;

    private final int postNums = 13;
    private final int size = 5;

    @BeforeEach
    public void setup() {
        for(int i = 1 ; i <= postNums ; i++) {
            Post post = Post.of("title"+i,"author"+i,"content"+i);
            boardService.savePost(post);
        }
    }

    @Test
    public void post_saveAndFind() throws Exception {
        //given
        Post post = Post.of("title", "author", "content");

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
        List<BoardPostListDto> dtoList = boardService.getBoardPostList(pageable);

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
        List<BoardPostListDto> dtoList = boardService.getBoardPostList(pageable);

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
        List<BoardPostListDto> dtoList = boardService.getBoardPostList(pageable);
        for (BoardPostListDto boardPostListDto : dtoList) {
            System.out.println("dto title = " + boardPostListDto.getTitle());
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
        List<BoardPostListDto> boardPostList = boardService.getBoardPostList(Pageable.ofSize(1));
        Long id = boardPostList.get(0).getId();
        Post findPost = boardService.findPostById(id);

        // when
        String title = "Updated Title";
        User guestAuthor = User.ofGuest("GuestAuthor");
        String content = "Updated Content";
        findPost.update(title, guestAuthor, content);
        boardService.savePost(findPost);

        // then
        Post updatedPost = boardService.findPostById(id);
        assertThat(updatedPost.getTitle()).isEqualTo(findPost.getTitle());
        assertThat(updatedPost.getAuthor()).isEqualTo(findPost.getAuthor());
        assertThat(updatedPost.getTitle()).isEqualTo(findPost.getTitle());
    }
}
