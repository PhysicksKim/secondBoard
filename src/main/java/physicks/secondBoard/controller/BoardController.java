package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import physicks.secondBoard.domain.board.dto.PostListDto;
import physicks.secondBoard.domain.board.dto.PostGuestUpdateDto;
import physicks.secondBoard.domain.board.dto.PostGuestWriteDto;
import physicks.secondBoard.domain.board.dto.PostReadDto;
import physicks.secondBoard.domain.board.service.BoardAuthenticationService;
import physicks.secondBoard.domain.board.service.BoardService;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.token.TokenDto;
import physicks.secondBoard.domain.user.AuthService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private static final String VIEW_PREFIX = "pages/board/";

    // 게시글 관련 인증로직
    private final BoardAuthenticationService boardAuthenticationService;

    private final AuthService authService;

    private final BoardService boardService;

    @GetMapping
    public String mainPage(Model model, Pageable pageable, Authentication authentication) {
        log.info("raw authentication : {}", authentication);

        List<PostListDto> postList = boardService.getPostListDtos(pageable);
        model.addAttribute("postList", postList);
        return VIEW_PREFIX + "board";
    }

    @GetMapping("/{id}")
    public String postRead(@PathVariable Long id, Model model) {
        PostReadDto dto = boardService.getPostReadDto(id);
        model.addAttribute("post", dto);
        return VIEW_PREFIX + "post";
    }

    @GetMapping("/write")
    public String postWritePage() {
        return VIEW_PREFIX + "write";
    }

    @GetMapping("/{id}/password")
    public String guestPostEditPasswordPage(@PathVariable Long id, Model model) {
        if(boardService.isGuestPost(id) == false) {
            // 회원 게시글에 게시글 비밀번호 페이지 요청을 한 경우 잘못된 접근임
            throw new IllegalArgumentException("잘못된 접근입니다");
        }

        model.addAttribute("postId", id);
        return VIEW_PREFIX + "guestPostPassword";
    }

    @PostMapping("/{id}/password")
    public String guestPostEditPasswordMatch(@PathVariable Long id, HttpServletResponse response, String password, RedirectAttributes redirectAttributes) {
        // 0. 해당 게시글이 회원 게시글인 경우에는 잘못된 접근 반환
        if(boardService.isGuestPost(id) == false) {
            // 회원 게시글에 패스워드 요청을 보내는 경우
            throw new IllegalArgumentException("잘못된 접근입니다");
        }

        // 1. 패스워드와 postId 를 넘겨서 게시글에 담긴 정보와 일치하는지 체크하고
        TokenDto tokenDto = boardService.validatePostPasswordAndGenerateToken(id, password);

        // 2. 일치하는 경우 jwt token 을 발급함. 회원인 경우에도 동일하게 "비회원 게시글 수정"에 대해서는 jwt token 을 통해 인증
        Cookie accessToken = new Cookie("accessToken", tokenDto.getAccessToken());
        // Cookie refreshToken = new Cookie("refreshToken", tokenDto.getRefreshToken());
        accessToken.setHttpOnly(true); // HTTP Only 설정
        accessToken.setSecure(true); // Secure 설정
        accessToken.setPath("/"); // 쿠키의 경로 설정
        accessToken.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효기간 설정 (예: 7일)

        // SameSite 설정 (Java 11 이상에서 지원. 낮은 버전의 Java에서는 별도의 처리가 필요)
        // accessToken.setSameSite("Strict"); // Strict, Lax, None 중 선택
        response.addCookie(accessToken);

        // 3. /{id}/edit 으로 redirect 해줌
        redirectAttributes.addAttribute("id", id);
        return "redirect:/board/{id}/edit";
    }

    /**
     * <h3>글 수정 페이지</h3>
     * 읽기 페이지에서 수정 버튼 클릭 시
     * 해당 게시글이 회원 게시글인지 비회원 게시글인지 우선 체크함.
     * 회원/비회원 따라서 다르게 동작함.
     * <pre>
     * 비회원 : /board/{id}/password
     *      비회원 게시글 패스워드 입력 페이지로 이동
     *      패스워드 입력 과정에서 authorize (1. 비회원 게시글인지 체크 2. 비회원 게시글인 경우 패스워드 일치 체크)
     *      권한 통과시 JWT token 발급해서 첨부하고 회원 페이지로 이동.
     * 회원 : /board/{id}/edit
     *      세션 기반 Spring Security 회원 검증 후에 일치 시 수정 페이지로 이동.
     * </pre>
     */
    @GetMapping("/{id}/edit")
    public String postUpdatePage(@PathVariable Long id, Authentication authentication, Model model) {
        boolean isGuestPost = boardService.isGuestPost(id);
        boolean isAuthNull = (authentication == null);
        // 비회원 게시글인 경우
        if (isGuestPost == true && isAuthNull == true) {
            // do guest post edit page logic
            // attach jwt token (access token + refresh token)
        }
        // 회원 게시글인 경우
        else if (isGuestPost == false && isAuthNull == false) {
            // validate whether auth instance matches with post user
            // if match then pass edit page
            // when edit requested, double-check session user
        } else {
            throw new IllegalArgumentException("잘못된 요청 입니다");
        }
        return VIEW_PREFIX + "write";
    }

    /**
     * from message 예시 <br>
     * @ResponseBody => title=testTitle&author=testAuthor&content=testContent
     */
    @PostMapping("/write")
    public String writePost(String title, String author, String content, HttpServletResponse response) {
        Post post;
        PostGuestWriteDto postGuestWriteDto = new PostGuestWriteDto(title, author, "password", content);

        try {
            post = boardService.savePost(postGuestWriteDto);
            log.info("save post successfully! : id = {}", post.getId());
            return "redirect:/board/"+post.getId();
        } catch (Exception e) {
            log.error("Error. postRepository.save(post) 에서 에러 발생 : {}", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "서버 오류로 글 작성에 실패했습니다. 잠시 후 다시 시도해 주세요.";
        }
    }

    @PostMapping("/write/{pathId}")
    public String updateGuestPost(@PathVariable Long pathId,
                             Long id, String title, String author, String content) {

        // !!! 수정 필요 !!!
        // - 일단은 간단하게 검증 함
        // 만약 post id 가 form hidden 에 없이 url만 날아온 경우
        // 잘못된 접근으로 판단해서 그냥 home 으로 redirect 한다
        //
        // - 검증 개선 방향
        // 어차피 postman 같은거로 /write/11 에서 보낸 것 처럼 출발 url 꾸미고
        // form data 도 다 위처럼 넣으면 이정도 필터링은 바로 뚫린다.
        // 그래서 사용할 방법이 JWT 토큰이다
        //
        // - JWT 토큰을 이용한 검증된 사용자 체크
        // JWT로 해당 게시글을 편집하는 사람이 유효한 사람인지 체크하자
        // 1. 게시글 -> 수정 버튼 클릭 -> "게시글 수정 비밀번호" 입력페이지를 응답해줌
        // 2. 사용자가 "게시글 수정 비밀번호" 검증을 통과하면, 수정 가능 JWT 토큰을 보냄.
        // 3. 다시, 수정된 게시글 내용이 담긴 Form Request가 돌아오면, JWT 토큰을 확인해서 유효한지 체크하도록 한다

        if(!boardAuthenticationService
                .isValidUpdateJWT(pathId, id)) {
            // ==========================================================
            // !!! 차후 "유효하지 않은 수정 요청입니다" 라는 페이지 보여주도록 수정 !!!
            // ==========================================================
            return "redirect:/";
        }

        // 임시로 Guest 권한으로 그냥 PostAuthor 집어넣었음.
        // 차후 회원 비회원 권한 구분이 필요하면 요청이 별도 컨트롤러로 분리되므로 그건 그때가서 생각
        PostGuestUpdateDto dto = new PostGuestUpdateDto(title, author, content);
        boardService.updatePost(id, dto);
        return "redirect:/board/" + id;
    }

    /**
     * 회원 게시글인 경우 auth != null
     * 비회원 게시글인 경우 auth == null
     *
     * @param authentication 회원 객체를 담음. 비회원인 경우 null.
     * @param isGuest        해당 게시글이 회원 게시글인지 여부를 담음
     * @return isGuest 여부에 따른 auth 객체가 매칭되는지 체크
     */
    private boolean isNotValidPostEditAuthArguments(Authentication authentication, boolean isGuest) {
        boolean isAuthNull = authentication == null;
        return (isAuthNull == isGuest); // 둘 다 true 또는 둘 다 false 인 경우 => true 반환
    }
}
