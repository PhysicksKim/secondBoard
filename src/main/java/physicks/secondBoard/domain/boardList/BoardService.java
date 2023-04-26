package physicks.secondBoard.domain.boardList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    PostRepository postRepository;

    public List<BoardPostListDto> getBoardPostList(Pageable pageable) {
        List<BoardPostListDto> result = new ArrayList<>();
        Page<Post> posts = postRepository.findAll(pageable);
        for (Post post : posts) {
            result.add(
                    BoardPostListDtoMapper.INSTANCE.toDto(post)
            );
        }
        return result;
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }
}
