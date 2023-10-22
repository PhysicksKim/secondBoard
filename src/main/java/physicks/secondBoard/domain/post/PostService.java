package physicks.secondBoard.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.author.Author;
import physicks.secondBoard.domain.author.AuthorRepository;
import physicks.secondBoard.domain.board.dto.PostGuestWriteDto;
import physicks.secondBoard.exception.RoleMismatchException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final AuthorRepository authorRepository;

    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    public Page<Post> getPostList(Pageable pageable) {
        return postRepository.findAllByOrderByIdDesc(pageable);
    }

    public List<Post> getPostAll() {
        return postRepository.findAll();
    }

    public Post savePost(PostGuestWriteDto dto) {
        Author author = Author.ofGuest(dto.getAuthor(), dto.getPassword());
        authorRepository.save(author);

        Post post = Post.of(dto.getTitle(), author, dto.getContent());
        return postRepository.save(post);
    }

    // public Post saveMemberPost(PostMemberWriteDto dto) {
    //
    // }

    public Post updateTitleAndContent(Long id, String title, String content) {
        Post post = postRepository.findById(id).get();
        post.updateTitleAndContent(title, content);
        return post;
    }

    public Post updateAuthorForGuest(Long id, String name) {
        Post post = postRepository.findById(id).get();
        if(!post.isGuest()) {
            throw new RoleMismatchException("비회원 게시글만 작성자 이름 변경이 가능합니다");
        }
        post.updateAuthor(name);
        return post;
    }
}

