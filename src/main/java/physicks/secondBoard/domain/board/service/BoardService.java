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
import physicks.secondBoard.domain.token.PostUpdateTokenService;
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
    private final PostUpdateTokenService postUpdateTokenService;

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
        return post.get();
    }

    public Post savePost(PostGuestWriteDto dto) {
        Post savedPost = postService.savePost(dto);
        return savedPost;
    }

    public TokenDto validatePostPasswordAndGenerateToken(long postId, String rawPassword) {
        Optional<Post> optionalPost = postService.findPostById(postId);
        if(optionalPost.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글 입니다");
        }

        Post findPost = optionalPost.get();
        if(findPost.isGuest() == false) {
            throw new IllegalArgumentException("회원 게시글은 게시글 비밀번호로 수정 권한을 요청할 수 없습니다");
        }

        if (!passwordEncoder.matches(rawPassword, findPost.getAuthor().getPassword())) {
            throw new IllegalArgumentException("일치하지 않는 비밀번호 입니다");
        }

        // 유효성 통과
        String accessToken = postUpdateTokenService.generateUpdateAccessToken(postId);
        String refreshToken = "리프래쉬 토큰 생성 로직 필요";

        return new TokenDto(accessToken, refreshToken);
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

    public List<Post> findAll() {
        // return postRepository.findAll();
        return postService.getPostAll();
    }
}
