package physicks.secondBoard.web.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.board.dto.PostListDto;
import physicks.secondBoard.domain.board.dto.PostWriteGuestDto;
import physicks.secondBoard.domain.board.dto.PostWriteMemberDto;
import physicks.secondBoard.domain.member.MemberRepository;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.user.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    private final int postNums = 13;
    private final int size = 5;

    @BeforeEach
    public void setup() {
        for(int i = 1 ; i <= postNums ; i++) {
            PostWriteGuestDto postWriteGuestDto = new PostWriteGuestDto("title" + i, "author" + i, "password" + i, "content" + i);
            boardService.writeGuestPost(postWriteGuestDto);
        }
    }

    @DisplayName("게시글 작성에 성공합니다.")
    @Test
    public void writeGuestPost_saveAndFind() throws Exception {
        //given
        PostWriteGuestDto postWriteGuestDto = new PostWriteGuestDto("title","author","password", "content");

        //when
        Post savedPost = boardService.writeGuestPost(postWriteGuestDto);
        em.flush();
        em.clear();

        Post foundPost = boardService.findPostById(savedPost.getId());

        //then
        assertThat(savedPost).isNotNull();
        assertThat(foundPost).isNotNull();

        assertThat(foundPost.getId()).isEqualTo(savedPost.getId());
        assertThat(foundPost.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(foundPost.getAuthor()).isEqualTo(savedPost.getAuthor());
        assertThat(foundPost.getContent()).isEqualTo(savedPost.getContent());
    }

    /**
     * Pagination 테스트 1 <br>
     * 최신 게시글 페이지 조회 테스트 <br>
     */
    @Test
    public void getBoardPostList_PaginationTest_NewestPage() throws Exception{
        //given
        final int page = 0;
        Pageable pageable = PageRequest.of(page, size);

        //when
        List<PostListDto> dtoList = boardService.getPostListDtos(pageable);

        //then
        assertThat(dtoList.size()).isEqualTo(size);
        for(int i = 0 ; i < size ; i++) {
            assertThat(dtoList.get(i).getTitle()).isEqualTo("title"+(postNums-i));
        }
    }

    // Pagination 테스트 2. 중간 페이지 조회 (ex. 총 3페이지 중 2페이지 조회)
    @Test
    public void getBoardPostList_PaginationTest_MiddlePage() throws Exception{
        //given
        final int page = 1;
        Pageable pageable = PageRequest.of(page, size);

        //when
        List<PostListDto> dtoList = boardService.getPostListDtos(pageable);

        //then
        assertThat(dtoList.size()).isEqualTo(size);
        for(int i = 0 ; i < size ; i++) {
            assertThat(dtoList.get(i).getTitle()).isEqualTo("title"+(postNums-i-size*page));
        }
    }


    // Pagination 테스트 3. 마지막 페이지 조회 (ex. 총 3페이지 중 3페이지 조회)
    @Test
    public void getBoardPostList_PaginationTest_OldestPage() throws Exception{
        //given
        final int page = 2;
        final int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        //when
        List<PostListDto> dtoList = boardService.getPostListDtos(pageable);
        for (PostListDto postListDto : dtoList) {
            System.out.println("dto title = " + postListDto.getTitle());
        }

        //then
        assertThat(dtoList.size()).isEqualTo(3);
        for(int i = 0 ; i < postNums%size ; i++) {
            assertThat(dtoList.get(i).getTitle()).isEqualTo("title"+(postNums-i-size*page));
        }
    }

    // todo : Post Entity Test 로 이동해야 합니다.
    @DisplayName("게시글 수정 요청에 성공합니다.")
    @Test
    public void postUpdateTest() throws Exception {
        // given
        List<PostListDto> boardPostList = boardService.getPostListDtos(Pageable.ofSize(1));
        Long id = boardPostList.get(0).getId();
        Post findPost = boardService.findPostById(id);

        // when
        String title = "Updated Title";
        String content = "Updated Content";
        String authorName = "GuestAuthor";

        findPost.updateTitleAndContent(title, content);
        findPost.getAuthor().updateName(authorName);

        em.flush();
        em.clear();

        // then
        Post updatedPost = boardService.findPostById(id);
        assertThat(updatedPost.getTitle()).isEqualTo(findPost.getTitle());
        assertThat(updatedPost.getAuthor().getAuthorName()).isEqualTo(findPost.getAuthor().getAuthorName());
        assertThat(updatedPost.getTitle()).isEqualTo(findPost.getTitle());
    }

    @DisplayName("올바른 request 에 따라서 guest post 작성에 성공")
    @Test
    void writeGuestPost() {
        // given
        String rawPassword = "123456a!";
        PostWriteGuestDto postWriteGuestDto = new PostWriteGuestDto("title", "author", rawPassword, "content");

        // when
        Post post = boardService.writeGuestPost(postWriteGuestDto);

        // then
        assertThat(post).isNotNull();
        assertThat(post.getId()).isNotNull();
        assertThat(post.getTitle()).isEqualTo(postWriteGuestDto.getTitle());
        assertThat(post.getAuthor().getAuthorName()).isEqualTo(postWriteGuestDto.getAuthorName());
        assertThat(post.getAuthor().isGuest()).isTrue();
        assertThat(post.getContent()).isEqualTo(postWriteGuestDto.getContent());
        assertTrue(passwordEncoder.matches(rawPassword, post.getAuthor().getPassword()));
    }

    @DisplayName("request 가 Empty String 이 들어있는 경우 예외를 던집니다.")
    @Test
    void writeGuestPost_failWithEmptyString() {
        // given
        PostWriteGuestDto postWriteGuestDto = new PostWriteGuestDto("","", "", "");

        // when
        Assertions.assertThrows(IllegalArgumentException.class, () -> boardService.writeGuestPost(postWriteGuestDto));
    }

    @DisplayName("회원 인증 객체 타입 UsernamePasswordAuthenticationToken 으로 회원 게시글 작성에 성공한다.")
    @Test
    void writeMemberPost_SuccessWithUsernamePasswordAuthenticationToken() {
        // given
        String rawPassword = "123456a!";
        Member member = Member.of(passwordEncoder.encode(rawPassword), "tester", "tester@test.com", false);
        memberRepository.save(member);

        Authentication authentication = authenticatedMemberToken();
        PostWriteMemberDto postWriteMemberDto = new PostWriteMemberDto("title", "content");

        log.info("authentication = {}", authentication);
        log.info("authentication.getName() = {}", authentication.getName());

        // when
        Post post = boardService.writeMemberPost(postWriteMemberDto, authentication);

        // then
        assertThat(post).isNotNull();
        assertThat(post.getId()).isNotNull();
        assertThat(post.getTitle()).isEqualTo(postWriteMemberDto.getTitle());
        assertThat(post.getAuthor().getAuthorName()).isEqualTo(member.getName());
        assertThat(post.getAuthor().isGuest()).isFalse();
        assertThat(post.getContent()).isEqualTo(postWriteMemberDto.getContent());
    }

    @DisplayName("request 가 비어있을 때 예외를 던집니다.")
    @Test
    void writeMemberPost_InvalidRequestData() {
        // Given
        PostWriteMemberDto invalidRequest = new PostWriteMemberDto("", "");
        Authentication authentication = authenticatedMemberToken();

        // When & Then
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThrows(IllegalArgumentException.class, () -> boardService.writeMemberPost(invalidRequest, authentication));
    }

    @DisplayName("인증되지 않은 회원 토큰이 회원 게시글 작성을 시도하면 예외를 던집니다.")
    @Test
    void writeMemberPost_AuthenticationNotFound() {
        // Given
        PostWriteMemberDto request = new PostWriteMemberDto("title", "content");
        Authentication authentication = unauthenticatedMemberToken();

        // When & Then
        assertThat(authentication.isAuthenticated()).isFalse();
        assertThrows(IllegalArgumentException.class, () -> boardService.writeMemberPost(request, authentication));
    }

    @DisplayName("비회원이 회원 게시글 작성을 시도하면 예외를 던집니다.")
    @Test
    void writeMemberPost_MemberNotFound() {
        // Given
        PostWriteMemberDto request = new PostWriteMemberDto("title", "content");
        Authentication authentication = guestToken();
        log.info("authentication.isAuthenticated() = {}", authentication.isAuthenticated());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> boardService.writeMemberPost(request, authentication));
    }

    @NotNull
    private static Authentication authenticatedMemberToken() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        return UsernamePasswordAuthenticationToken.authenticated("tester@test.com", "", authorities);
    }

    @NotNull
    private static Authentication unauthenticatedMemberToken() {
        return UsernamePasswordAuthenticationToken.unauthenticated("tester@test.com", "");
    }

    @NotNull
    private static Authentication guestToken() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_GUEST");
        Authentication authentication = new AnonymousAuthenticationToken("guest", "guest", authorities);
        return authentication;
    }

}
