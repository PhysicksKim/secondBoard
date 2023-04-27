package physicks.secondBoard.domain.board;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BoardService {

    @Autowired
    PostRepository postRepository;

    public List<BoardPostListDto> getBoardPostList(Pageable pageable) {
        List<BoardPostListDto> result = new ArrayList<>();
        Page<Post> posts = postRepository.findAllByOrderByIdDesc(pageable);
        for (Post post : posts) {
            log.info("Post title : {}", post.getTitle());
            BoardPostListDto dto = BoardPostListDtoMapper.INSTANCE.toDto(post);
            result.add(dto);

            log.info("Dto title : {}", dto.getTitle());
        }
        return result;
    }

    public Post getPostById(long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.get();
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }
}
