package physicks.secondBoard.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.board.dto.PostGuestWriteDto;
import physicks.secondBoard.domain.board.dto.PostListDto;
import physicks.secondBoard.domain.board.dto.PostReadDto;
import physicks.secondBoard.domain.board.dto.PostUpdatePageDto;
import physicks.secondBoard.domain.board.dto.mapper.PostListDtoMapper;
import physicks.secondBoard.domain.board.dto.mapper.PostReadDtoMapper;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostService;
import physicks.secondBoard.domain.token.PostEditTokenService;
import physicks.secondBoard.domain.token.TokenDto;
import physicks.secondBoard.web.controller.request.PostUpdateRequestDto;

import java.util.ArrayList;
import java.util.List;

/**
 * need to refactor this class
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final PostService postService;
    private final PostEditTokenService postEditTokenService;

    private final PasswordEncoder passwordEncoder;

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
        Post post = postService.findPostById(id);
        return PostReadDtoMapper.toDto(post);
    }

    public boolean isGuestPost(long id) {
        Post post = findPostById(id);
        return post.getAuthor().isGuest();
    }

    public Post findPostById(long id) {
        return postService.findPostById(id);
    }

    public Post writePost(PostGuestWriteDto dto) {
        return postService.createPostOfGuest(dto.getTitle(), dto.getAuthor(), dto.getPassword(), dto.getContent());
    }

    public boolean isValidEditAccessToken(long postId, String accessToken) {
        try {
            return postEditTokenService.validateEditAccessToken(accessToken, postId);
        } catch (Exception e) {
            log.error("유효하지 않은 토큰 인증 요청 발생", e);
            throw new IllegalArgumentException("유효하지 않은 토큰입니다");
        }
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
        Post findPost = postService.findPostById(postId);
        if (!passwordEncoder.matches(rawPassword, findPost.getAuthor().getPassword())) {
            log.error("비밀번호가 일치하지 않습니다 :: rawPassword = {}", rawPassword);
            log.error("비밀번호가 일치하지 않습니다 :: post password = {}", findPost.getAuthor().getPassword());
            throw new IllegalArgumentException("일치하지 않는 비밀번호 입니다");
        }

        // -- 유효성 통과 후 --
        String accessToken = postEditTokenService.generateEditAccessToken(postId);
        String refreshToken = postEditTokenService.generateEditRefreshToken(postId);

        return new TokenDto(accessToken, refreshToken);
    }

    /**
     * 게시글을 비밀번호 입력 방식으로 수정할 수 있는지 체크함. 회원 게시글은 비밀번호 방식으로 수정할 수 없음.
     * @param postId 대상 post
     */
    private Post canEditPostByPassword(long postId) {
        Post findPost = postService.findPostById(postId);
        if(findPost.getAuthor().isGuest() == false) {
            throw new IllegalArgumentException("회원 게시글은 게시글 비밀번호로 수정 권한을 요청할 수 없습니다");
        }

        return findPost;
    }

    public TokenDto generateNewTokensByRefreshToken(long postId, String refreshToken) {
        if(!postEditTokenService.validateEditRefreshToken(refreshToken, postId)) {
            throw new IllegalArgumentException("유효하지 않은 리프래쉬 토큰입니다");
        }

        // 기존 refresh token 파기 로직 구현 필요

        String newAccessToken = postEditTokenService.generateEditAccessToken(postId);
        String newRefreshToken = postEditTokenService.generateEditRefreshToken(postId);
        TokenDto tokenDto = new TokenDto(newAccessToken, newRefreshToken);
        return tokenDto;
    }

    /**
     * 게시글 수정
     * BoardService 에서는 PostService 로 수정을 위임합니다.
     * Post 수정과 관련된 로직은 모두 postService 가 가지고 있도록 합니다.
     * BoardService 는 dto 와 관련된 의존성 해결에 집중합니다.
     * PostService 는 Entity 와 관련된 의존성 해결에 집중합니다.
     * @param dto
     * @return
     */
    public Post updatePost(PostUpdateRequestDto dto) {
        postService.updateTitleAndContent(dto.getId(), dto.getTitle(), dto.getContent());
        postService.updateAuthorForGuest(dto.getId(), dto.getAuthor());
        return postService.findPostById(dto.getId());
    }
}
