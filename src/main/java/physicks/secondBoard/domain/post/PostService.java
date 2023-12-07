package physicks.secondBoard.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.author.AuthorRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    public Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다"));
    }

    public Page<Post> getPostList(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedTimeDesc(pageable);
    }

    /**
     * 비회원 게시글을 신규 작성합니다
     * @param title
     * @param author
     * @param password rawPassword 를 받습니다. 인코딩은 이 메서드 내에서 수행합니다.
     * @param content
     * @return
     */
    public Post createPostOfGuest(String title, String author, String password, String content) {
        String encodedPassword = passwordEncoder.encode(password);

        Author authorEntity = Author.ofGuest(author, encodedPassword);
        authorRepository.save(authorEntity);

        Post post = Post.of(title, authorEntity, content);
        return postRepository.save(post);
    }

    public Post updateTitleAndContent(Long id, String title, String content) {
        Post post = postRepository.findById(id).get();
        post.updateTitleAndContent(title, content);
        return post;
    }

    public Post updateAuthorForGuest(Long id, String name) {
        Post post = postRepository.findById(id).get();
        if(!post.getAuthor().isGuest()) {
            throw new IllegalArgumentException("비회원 게시글만 작성자 이름 변경이 가능합니다");
        }
        post.getAuthor().updateName(name);
        return post;
    }
}

