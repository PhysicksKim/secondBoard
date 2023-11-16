package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import physicks.secondBoard.domain.board.dto.*;
import physicks.secondBoard.domain.board.service.BoardAuthenticationService;
import physicks.secondBoard.domain.board.service.BoardService;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.token.TokenDto;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private static final String VIEW_PREFIX = "pages/board/";

    // 게시글 관련 인증로직
    private final BoardAuthenticationService boardAuthenticationService;

    private final BoardService boardService;

    @GetMapping
    public String mainPage(Model model, Pageable pageable, Authentication authentication) {
        log.info("raw authentication : {}", authentication);

        List<PostListDto> postList = boardService.getPostListDtos(pageable);
        model.addAttribute("postList", postList);
        return VIEW_PREFIX + "board";
    }

    @GetMapping("/{postId}")
    public String postRead(@PathVariable Long postId, Model model) {
        PostReadDto dto = boardService.getPostReadDto(postId);
        model.addAttribute("post", dto);
        return VIEW_PREFIX + "post";
    }

    @GetMapping("/write")
    public String postWritePage() {
        return VIEW_PREFIX + "write";
    }

    @GetMapping("/{postId}/password")
    public String guestPostEditPasswordPage(@PathVariable Long postId, Model model) {
        if(boardService.isGuestPost(postId) == false) {
            // 회원 게시글에 게시글 비밀번호 페이지 요청을 한 경우 잘못된 접근임
            throw new IllegalArgumentException("잘못된 접근입니다");
        }

        model.addAttribute("postId", postId);
        return VIEW_PREFIX + "guestPostPassword";
    }

    @PostMapping("/{postId}/password")
    public String guestPostEditPasswordMatch(@PathVariable Long postId, HttpServletResponse response, String password, RedirectAttributes redirectAttributes) {
        // 0. 해당 게시글이 회원 게시글인 경우에는 잘못된 접근 반환
        if(boardService.isGuestPost(postId) == false) {
            // 회원 게시글에 패스워드 요청을 보내는 경우
            throw new IllegalArgumentException("잘못된 접근입니다");
        }

        // 1. 패스워드와 postId 를 넘겨서 게시글에 담긴 정보와 일치하는지 체크하고 토큰을 받아옴
        TokenDto tokenDto = boardService.validatePostPasswordAndGenerateToken(postId, password);

        // 2. jwt token 을 쿠키에 넣어줌
        addTokenCookie(response, tokenDto, postId);

        // 3. /{postId}/edit 으로 redirect 해줌
        redirectAttributes.addAttribute("id", postId);
        return "redirect:/board/{postId}/edit";
    }

    /**
     * <h3>글 수정 페이지</h3>
     * 읽기 페이지에서 수정 버튼 클릭 시
     * 해당 게시글이 회원 게시글인지 비회원 게시글인지 우선 체크함.
     * 회원/비회원 따라서 다르게 동작함.
     * <pre>
     * 비회원 : /board/{postId}/password
     *      비회원 게시글 패스워드 입력 페이지로 이동
     *      패스워드 입력 과정에서 authorize (1. 비회원 게시글인지 체크 2. 비회원 게시글인 경우 패스워드 일치 체크)
     *      권한 통과시 JWT token 발급해서 첨부하고 회원 페이지로 이동.
     * 회원 : /board/{postId}/edit
     *      세션 기반 Spring Security 회원 검증 후에 일치 시 수정 페이지로 이동.
     * </pre>
     */
    @GetMapping("/{postId}/edit")
    public String postUpdatePage(@PathVariable Long postId,
                                 @CookieValue("EditAccessToken") String accessToken,
                                 @CookieValue("EditRefreshToken") String refreshToken,
                                 Authentication authentication,
                                 Model model) {
        if(accessToken == null || accessToken.equals("")) {
            throw new IllegalArgumentException("잘못된 접근입니다");
        }

        log.info("Edit Access Token : {}", accessToken);

        // accessToken 로직 수행
        if (!boardService.isValidEditAccessToken(postId, accessToken)) {
            // 토큰이 유효하지 않은 경우 401 Unauthorized 를 응답 => 클라이언트는 401을 확인 후 refresh 재발급 요청을 수행함
            log.info("토큰이 유효하지 않음");
        }

        boolean isGuestPost = boardService.isGuestPost(postId);
        boolean isAuthNull = (authentication == null);

        PostUpdatePageDto dto = null;
        // 비회원 게시글인 경우
        if (isGuestPost == true && isAuthNull == true) {
            // do guest post edit page logic
            // attach jwt token (access token + refresh token)
            dto = boardService.getPostUpdatePageDtoByToken(postId, accessToken);
            log.info("비회원 게시글 dto 생성 :: {}", dto);
        }

        // 회원 게시글인 경우
        else if (isGuestPost == false && isAuthNull == false) {
            // validate whether auth instance matches with post user
            // if match then pass edit page
            // when edit requested, double-check session user
            log.info("수정 페이지 요청에서 회원 게시글 분기로 들어감");
        } else {
            throw new IllegalArgumentException("잘못된 요청 입니다");
        }

        model.addAttribute("post", dto);
        return VIEW_PREFIX + "write";
    }

    /**
     * 토큰 재발급
     * @param postId
     * @param refreshToken
     * @param response
     * @return
     */
    @PostMapping("/{postId}/edit/tokenRefresh")
    public ResponseEntity postEditTokenRefresh(@PathVariable long postId,
                                               @CookieValue("EditRefreshToken") String refreshToken,
                                               HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("잘못된 접근 입니다");
        }

        TokenDto tokenDto = boardService.generateNewTokensByRefreshToken(postId, refreshToken);
        addTokenCookie(response, tokenDto, postId);

        // 상태코드 200
        return ResponseEntity.ok().build();
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

    @PostMapping("/write/{postId}")
    public String updateGuestPost_DEPRECATED(@PathVariable Long postId,
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
                .isValidUpdateJWT(postId, id)) {
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

    @PostMapping("/{postId}/edit")
    public String updateGuestPost(@PathVariable long postId,
                                  @ModelAttribute PostUpdateRequestDto dto,
                                  @CookieValue("EditAccessToken") String accessToken,
                                  RedirectAttributes redirectAttributes) {
        if (postId != dto.getId()) {
            throw new IllegalArgumentException("URL 의 PostId 와 요청으로 전달된 PostId 가 일치하지 않습니다");
        }
        if (!boardService.isValidEditAccessToken(postId, accessToken)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다");
        }

        Post post = boardService.updatePost(dto);
        redirectAttributes.addAttribute("postId", post.getId());
        return "redirect:/board/{postId}/";
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

    private void addTokenCookie(HttpServletResponse response, TokenDto tokenDto, long postId) {
        String rawAccessToken = URLEncoder.encode(tokenDto.getAccessToken(), StandardCharsets.UTF_8);
        String rawRefreshToken = URLEncoder.encode(tokenDto.getRefreshToken(), StandardCharsets.UTF_8);
        log.info("add Cookie \t:: Access token = {} \n \t\t:: Refresh Token = {}", rawAccessToken, rawRefreshToken);
        ResponseCookie accessToken = ResponseCookie.from("EditAccessToken", rawAccessToken)
                .httpOnly(true)
                .sameSite("strict")
                .secure(true)
                .path("/board/"+postId)
                .maxAge(Duration.ofSeconds(60*10))
                .build();
        ResponseCookie refreshToken = ResponseCookie.from("EditRefreshToken", rawRefreshToken)
                .httpOnly(true)
                .sameSite("strict")
                .secure(true)
                .path("/board/"+postId)
                .maxAge(Duration.ofSeconds(60*11))
                .build();
        response.addHeader("set-cookie", accessToken.toString());
        response.addHeader("set-cookie", refreshToken.toString());
    }
}
