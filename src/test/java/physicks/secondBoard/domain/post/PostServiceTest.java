package physicks.secondBoard.domain.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

// todo : Post Update 관련 테스트 필요.
@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @DisplayName("회원 게시글을 작성합니다")
    @Test
    void createPostOfGuest() {
        // given
        String title = "testTitle";
        String author = "guest1";
        String password = "Password1!";
        String content = "hello this is test post";

        // when
        Post postOfGuest = postService.createPostOfGuest(title, author, password, content);

        // then
        assertThat(postOfGuest).isNotNull();
        assertThat(postOfGuest.getTitle()).isEqualTo(title);
        assertThat(postOfGuest.getAuthor().getAuthorName()).isEqualTo(author);
        assertThat(postOfGuest.getContent()).isEqualTo(content);
        // 패스워드는 인코딩 되므로 NotEqual
        assertThat(postOfGuest.getAuthor().getPassword()).isNotEqualTo(password);
    }

    @DisplayName("게시글을 생성하고 id 로 찾아옵니다.")
    @Test
    void findPostById() {
        // given
        String title = "testTitle";
        String author = "guest1";
        String password = "Password1!";
        String content = "hello this is test post";

        Post postOfGuest = postService.createPostOfGuest(title, author, password, content);

        // when
        Post findPost = postService.findPostById(postOfGuest.getId());

        // then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(title);
        assertThat(findPost.getAuthor().getAuthorName()).isEqualTo(author);
        assertThat(findPost.getContent()).isEqualTo(content);
        // 패스워드는 인코딩 되므로 NotEqual
        assertThat(findPost.getAuthor().getPassword()).isNotEqualTo(password);
    }

    @DisplayName("게시글 리스트를 가져옵니다")
    @Test
    void getPostList() {
        // given
        generateSamplePosts(20);

        // when
        Page<Post> postList = postService.getPostList(PageRequest.of(0, 10));

        // then
        assertThat(postList).isNotNull();
        assertThat(postList.getTotalElements()).isEqualTo(20);
        assertThat(postList.getTotalPages()).isEqualTo(2);
        assertThat(postList.getNumber()).isEqualTo(0);
        assertThat(postList.getSize()).isEqualTo(10);
    }

    private void generateSamplePosts(int numberOfPosts) {
        for (int i = 1; i <= numberOfPosts; i++) {
            String title = "testTitle" + i;
            String author = "guest" + i;
            String password = "Password" + i + "!";
            String content = "hello this is test post" + i;

            postService.createPostOfGuest(title, author, password, content);
        }
    }


}