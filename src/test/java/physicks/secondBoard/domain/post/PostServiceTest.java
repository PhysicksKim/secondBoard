package physicks.secondBoard.domain.post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.post.author.Author;
import physicks.secondBoard.domain.user.Member;
import physicks.secondBoard.web.service.AuthorService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorService authorService;

    @DisplayName("비회원 게시글을 작성합니다")
    @Test
    void createPostOfGuest() {
        // given
        String title = "testTitle";
        String author = "guest1";
        String password = "Password1!";
        String content = "hello this is test post";

        // when
        Author guest = authorService.createGuestAuthor(author, password);
        Post postOfGuest = postService.createPost(title, guest, content);

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

        Author guest = authorService.createGuestAuthor(author, password);
        Post postOfGuest = postService.createPost(title, guest, content);

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

    @DisplayName("게시글의 제목과 내용을 수정합니다")
    @Test
    void updateTitleAndContent() {
        // given
        generateSamplePosts(1);
        Page<Post> list = postService.getPostList(PageRequest.of(0, 1));
        Post post = list.getContent().get(0);
        long id = post.getId();

        // when
        String newTitle = "newTitle";
        String newContent = "newContent";
        Post updatedPost = postService.updateTitleAndContent(id, newTitle, newContent);

        // then
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getTitle()).isEqualTo(newTitle);
        assertThat(updatedPost.getContent()).isEqualTo(newContent);
    }

    @DisplayName("비회원 게시글은 Author name 수정이 가능합니다")
    @Test
    void updateAuthorForGuest() {
        // given
        generateSamplePosts(1);
        Page<Post> list = postService.getPostList(PageRequest.of(0, 1));
        Post post = list.getContent().get(0);
        long id = post.getId();

        // when
        String newName = "newName";
        Post updatedPost = postService.updateAuthorForGuest(id, newName);

        // then
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getAuthor().getAuthorName()).isEqualTo(newName);
    }

    @DisplayName("회원 게시글을 작성합니다.")
    @Test
    void createPostOfMember_success() {
        // given
        String title = "testTitle";
        String content = "hello this is test post";

        Member member = generateMember();
        Author memberAuthor = authorService.createMemberAuthor(member);

        // when
        Post postOfMember = postService.createPostOfMember(title, member, content);

        // then
        Assertions.assertThat(postOfMember).isNotNull();
        Assertions.assertThat(postOfMember.getTitle()).isEqualTo(title);
        Assertions.assertThat(postOfMember.getAuthor()).isEqualTo(memberAuthor);
        Assertions.assertThat(postOfMember.getContent()).isEqualTo(content);
    }

    private Member generateMember() {
        String email = "tester@test.com";
        String password = "Password1!";
        String name = "tester";
        String encodedPassword = passwordEncoder.encode(password);
        boolean isOauthUser = false;

        return Member.of(email, encodedPassword, name, isOauthUser);
    }

    private void generateSamplePosts(int numberOfPosts) {
        for (int i = 1; i <= numberOfPosts; i++) {
            String title = "testTitle" + i;
            String authorName = "guest" + i;
            String password = "Password" + i + "!";
            String content = "hello this is test post" + i;

            Author guest = authorService.createGuestAuthor(authorName, password);
            postService.createPost(title, guest, content);
        }
    }

}