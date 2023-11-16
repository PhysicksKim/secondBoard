package physicks.secondBoard.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.board.dto.*;
import physicks.secondBoard.domain.board.mapper.PostListDtoMapper;
import physicks.secondBoard.domain.board.mapper.PostReadDtoMapper;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostService;
import physicks.secondBoard.domain.token.PostEditTokenService;
import physicks.secondBoard.domain.token.TokenDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final PostService postService;
    private final PasswordEncoder passwordEncoder;
    private final PostEditTokenService postEditTokenService;

    public List<PostListDto> getPostListDtos(Pageable pageable) {
        List<PostListDto> result = new ArrayList<>();
        Page<Post> posts = postService.getPostList(pageable);
        for (Post post : posts) {
            PostListDto dto = PostListDtoMapper.toDto(post);
            result.add(dto);
        }
        return result;
    }

    public PostReadDto getPostReadDto(long id) {
        Post post = findPostById(id);
        return PostReadDtoMapper.toDto(post);
    }

    public boolean isGuestPost(long id) {
        Post post = findPostById(id);
        return post.getAuthor().isGuest();
    }

    public Post findPostById(long id) {
        Optional<Post> post = postService.findPostById(id);
        if(post.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글 입니다");
        }
        return post.get();
    }

    public Post savePost(PostGuestWriteDto dto) {
        Post savedPost = postService.savePost(dto);
        return savedPost;
    }

    public boolean isValidEditAccessToken(long postId, String accessToken) {
        return postEditTokenService.validateEditAccessToken(accessToken, postId);
    }

    public PostUpdatePageDto getPostUpdatePageDtoByToken(long postId, String accessToken) {
        if (!postEditTokenService.validateEditAccessToken(accessToken, postId)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다");
        }

        Post findPost = findPostById(postId);
        return new PostUpdatePageDto(findPost.getId(), findPost.getTitle(), findPost.getAuthor().getAuthorName(), findPost.getContent());
    }

    public TokenDto validatePostPasswordAndGenerateToken(long postId, String rawPassword) {
        canEditPostByPassword(postId);

        Post findPost = postService.findPostById(postId).get();

        if (!passwordEncoder.matches(rawPassword, findPost.getAuthor().getPassword())) {
            throw new IllegalArgumentException("일치하지 않는 비밀번호 입니다");
        }

        // 유효성 통과
        String accessToken = postEditTokenService.generateEditAccessToken(postId);
        String refreshToken = postEditTokenService.generateEditRefreshToken(postId);

        return new TokenDto(accessToken, refreshToken);
    }

    /**
     * 게시글을 비밀번호 입력 방식으로 수정할 수 있는지 체크함
     * @param postId 대상 post
     */
    private Post canEditPostByPassword(long postId) {
        Optional<Post> optionalPost = postService.findPostById(postId);
        if(optionalPost.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글 입니다");
        }

        Post findPost = optionalPost.get();
        if(findPost.isGuest() == false) {
            throw new IllegalArgumentException("회원 게시글은 게시글 비밀번호로 수정 권한을 요청할 수 없습니다");
        }

        return findPost;
    }

    public TokenDto generateNewTokensByRefreshToken(long postId, String refreshToken) {
        if(!postEditTokenService.validateEditRefreshToken(refreshToken, postId)) {
            throw new IllegalArgumentException("유효하지 않은 리프래쉬 토큰입니다");
        }

        // 기존 refresh token 파기 로직

        String newAccessToken = postEditTokenService.generateEditAccessToken(postId);
        String newRefreshToken = postEditTokenService.generateEditRefreshToken(postId);
        TokenDto tokenDto = new TokenDto(newAccessToken, newRefreshToken);
        return tokenDto;
    }

    public Post updatePost(Long id, PostGuestUpdateDto dto) {
        Post post = postService.findPostById(id).get();
        post.updateTitleAndContent(dto.getTitle(), dto.getContent());
        post.updateAuthor(dto.getName());

        return post;
    }

    public Post updatePost(Long id, PostMemberUpdateDto dto) {
        Post post = postService.findPostById(id).get();
        post.updateTitleAndContent(dto.getTitle(), dto.getContent());

        return post;
    }

    public Post updatePost(PostUpdateRequestDto dto) {
        Post post = postService.findPostById(dto.getId()).get();
        log.info("post 수정 전 :: {}", post);
        post.updateTitleAndContent(dto.getTitle(), dto.getContent());
        log.info("post 수정 후 :: {}", post);
        return post;
    }

    public List<Post> findAll() {
        // return postRepository.findAll();
        return postService.getPostAll();
    }
}
