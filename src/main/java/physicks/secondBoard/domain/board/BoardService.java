package physicks.secondBoard.domain.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.entity.AuditBaseEntity;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final PostRepository postRepository;

    public List<BoardPostListDto> getBoardPostList(Pageable pageable) {
        List<BoardPostListDto> result = new ArrayList<>();
        Page<Post> posts = postRepository.findAllByOrderByIdDesc(pageable);
        for (Post post : posts) {
            BoardPostListDto dto = BoardPostListDtoMapper.INSTANCE.toDto(post);
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

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
