package physicks.secondBoard.domain.post.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.board.PostGuestWriteDto;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;
import physicks.secondBoard.exception.RoleMismatchException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestPostService {

    private final PostRepository postRepository;

    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    public Page<Post> getPostList(Pageable pageable) {
        return postRepository.findAllByOrderByIdDesc(pageable);
    }

    public Post savePost(PostGuestWriteDto dto) {
        Author author = Author.ofGuest(dto.getAuthor(), dto.getPassword());
        Post post = Post.of(dto.getTitle(), author, dto.getContent());
        return postRepository.save(post);
    }

    public Post updateTitleAndContent(Long id, String title, String content) {
        Post post = postRepository.findById(id).get();
        post.updateTitleAndContent(title, content);
        return post;
    }

    public Post updateAuthorForGuest(Long id, String nickname) {
        Post post = postRepository.findById(id).get();
        if(!post.isGuest()) {
            throw new RoleMismatchException("비회원 게시글만 작성자 이름 변경이 가능합니다");
        }
        post.updateAuthor(nickname);
        return post;
    }
}
