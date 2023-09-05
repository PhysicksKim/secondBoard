package physicks.secondBoard.domain.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;
import physicks.secondBoard.domain.post.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final PostRepository postRepository;

    private final PostService postService;

    public List<BoardPostDto> getBoardPostList(Pageable pageable) {
        List<BoardPostDto> result = new ArrayList<>();
        Page<Post> posts = postRepository.findAllByOrderByIdDesc(pageable);
        for (Post post : posts) {
            BoardPostDto dto = BoardPostDtoMapper.toDto(post);
            result.add(dto);
        }
        return result;
    }

    public Post findPostById(long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.get();
    }

    public Post savePost(PostGuestWriteDto dto) {
        Post savedPost = postService.savePost(dto);
        return savedPost;
    }

    public Post updatePost(Long id, PostGuestUpdateDto dto) {
        Post post = postRepository.findById(id).get();
        post.updateTitleAndContent(dto.getTitle(), dto.getContent());
        post.updateAuthor(dto.getNickname());

        return post;
    }

    public Post updatePost(Long id, PostMemberUpdateDto dto) {
        Post post = postRepository.findById(id).get();
        post.updateTitleAndContent(dto.getTitle(), dto.getContent());

        return post;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
