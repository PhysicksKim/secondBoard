package physicks.secondBoard.domain.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final PostRepository postRepository;

    private final BoardPostListDtoMapper boardPostListDtoMapper;

    public List<BoardPostListDto> getBoardPostList(Pageable pageable) {
        List<BoardPostListDto> result = new ArrayList<>();
        Page<Post> posts = postRepository.findAllByOrderByIdDesc(pageable);
        for (Post post : posts) {
            BoardPostListDto dto = boardPostListDtoMapper.toDto(post);
            result.add(dto);
        }
        return result;
    }

    public Post findPostById(long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.get();
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, String title, String author, String content) {
        Post post = postRepository.findById(id).get();
        post.update(title, author, content);
        return post;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
