package physicks.secondBoard.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.board.dto.*;
import physicks.secondBoard.domain.board.dto.mapper.PostListDtoMapper;
import physicks.secondBoard.domain.board.dto.mapper.PostReadDtoMapper;
import physicks.secondBoard.domain.member.AuthenticationUtils;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostService;
import physicks.secondBoard.domain.post.author.Author;
import physicks.secondBoard.domain.token.PostEditTokenService;
import physicks.secondBoard.domain.token.TokenDto;
import physicks.secondBoard.domain.user.Member;
import physicks.secondBoard.web.controller.request.PostUpdateRequestDto;

import java.util.List;


// TODO : 리팩토링. 1. 코드 가독성 향상 필요 2. 위임 구조 점검 필요 3. request 에 일대일 매칭 되도록 퍼사드 구조 점검
/**
 * BoardController 를 위한 facade Service 입니다.
 * 각각의 public 메서드는 하나 또는 최소한의 controller mapping 과 매칭됩니다.
 * 예외적으로, 간단한 위임 메서드는 여러 곳에서 호출될 수 있습니다. (ex. isGuestPost, findPostById)
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final PostService postService;
    private final MemberService memberService;
    private final PostEditTokenService postEditTokenService;
    private final AuthorService authorService;
    private final AuthenticationUtils authenticationUtils;

    public List<PostListDto> getPostListDtos(Pageable pageable) {
        return postService.getPostList(pageable)
                .stream()
                .map(PostListDtoMapper::toDto)
                .toList();
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

    public Post writeGuestPost(PostWriteGuestRequest dto) {
        if(invalidWriteRequest(dto)) {
            throw new IllegalArgumentException("게시글 작성 요청 값이 유효하지 않습니다.");
        }

        Author guestAuthor = authorService.createGuestAuthor(dto.getAuthorName(), dto.getPassword());
        return postService.createPost(dto.getTitle(), guestAuthor, dto.getContent());
    }

    // todo : test 작성 필요
    public Post writeMemberPost(PostWriteMemberRequest dto, Authentication authentication) {
        String email = authenticationUtils.extractEmail(authentication);
        Member member = memberService.findMemberByEmail(email);
        Author memberAuthor = authorService.createMemberAuthor(member);
        return postService.createPost(dto.getTitle(), memberAuthor, dto.getContent());
    }

    public PostUpdatePageDto getPostUpdatePageDtoUsingAccessToken(long postId, String accessToken) {
        if (!postEditTokenService.validateEditAccessToken(accessToken, postId)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다");
        }

        if(!postEditTokenService.validateEditAccessToken(accessToken, postId)) {
            throw new IllegalArgumentException("게시글 수정 토큰이 유효하지 않습니다");
        }

        Post findPost = findPostById(postId);
        return new PostUpdatePageDto(findPost.getId(), findPost.getTitle(), findPost.getAuthor().getAuthorName(), findPost.getContent());
    }

    public TokenDto validatePostPasswordAndGenerateTokens(long postId, String rawPassword) {
        checkCanEditWithPassword(postId);
        Post findPost = postService.findPostById(postId);
        if (!authorService.isMatchedPassword(findPost.getAuthor(), rawPassword)) {
            log.info("비밀번호가 일치하지 않습니다 :: rawPassword = {}", rawPassword);
            log.info("비밀번호가 일치하지 않습니다 :: post password = {}", findPost.getAuthor().getPassword());
            throw new IllegalArgumentException("일치하지 않는 비밀번호 입니다");
        }

        // -- 유효성 통과 후 --
        String accessToken = postEditTokenService.generateEditAccessToken(postId);
        String refreshToken = postEditTokenService.generateEditRefreshToken(postId);

        return new TokenDto(accessToken, refreshToken);
    }

    public TokenDto generateNewTokensUsingRefreshToken(long postId, String refreshToken) {
        if(!postEditTokenService.validateEditRefreshToken(refreshToken, postId)) {
            throw new IllegalArgumentException("유효하지 않은 리프래쉬 토큰입니다");
        }

        // 기존 refresh token 파기 로직 구현 필요

        String newAccessToken = postEditTokenService.generateEditAccessToken(postId);
        String newRefreshToken = postEditTokenService.generateEditRefreshToken(postId);
        return new TokenDto(newAccessToken, newRefreshToken);
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
    public Post updatePostUsingAccessToken(PostUpdateRequestDto dto, String token) {
        if (!postEditTokenService.validateEditAccessToken(token, dto.getId())) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다");
        }

        postService.updateTitleAndContent(dto.getId(), dto.getTitle(), dto.getContent());
        postService.updateAuthorForGuest(dto.getId(), dto.getAuthor());
        return postService.findPostById(dto.getId());
    }

    /**
     * 게시글을 비밀번호 입력 방식으로 수정할 수 있는지 체크함. 회원 게시글은 비밀번호 방식으로 수정할 수 없음.
     * @param postId 대상 post
     */
    private void checkCanEditWithPassword(long postId) {
        Post findPost = postService.findPostById(postId);
        if(!findPost.getAuthor().isGuest()) {
            throw new IllegalArgumentException("회원 게시글은 게시글 비밀번호로 수정 권한을 요청할 수 없습니다");
        }
    }

    private boolean invalidWriteRequest(PostWriteGuestRequest dto) {
        return dto.getTitle() == null || dto.getAuthorName() == null || dto.getPassword() == null || dto.getContent() == null ||
                dto.getTitle().isEmpty() || dto.getAuthorName().isEmpty() || dto.getPassword().isEmpty() || dto.getContent().isEmpty();
    }
}
