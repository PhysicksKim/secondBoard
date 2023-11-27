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
import physicks.secondBoard.domain.board.service.BoardService;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.token.TokenDto;

import jakarta.servlet.http.HttpServletResponse;
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

    private final BoardService boardService;

    @GetMapping
    public String showMainPage(Model model, Pageable pageable, Authentication authentication) {
        List<PostListDto> postList = boardService.getPostListDtos(pageable);
        model.addAttribute("postList", postList);
        return VIEW_PREFIX + "board";
    }

    @GetMapping("/{postId}")
    public String showPost(@PathVariable Long postId, Model model) {
        PostReadDto dto = boardService.getPostReadDto(postId);
        model.addAttribute("post", dto);
        return VIEW_PREFIX + "post";
    }

    /**
     * 게시글 작성 페이지를 보여줍니다.
     * 캐싱을 막기 위해 캐싱 헤더를 추가합니다.
     * @param response
     * @return
     */
    @GetMapping("/write")
    public String showWritePostForm(HttpServletResponse response) {
        noCachePage(response);
        return VIEW_PREFIX + "write";
    }

    @GetMapping("/{postId}/password")
    public String showGuestPostPasswordForm(@PathVariable Long postId,
                                            HttpServletResponse response,
                                            Model model) {
        if(boardService.isGuestPost(postId) == false) {
            throw new IllegalArgumentException("회원 게시글은 게시글 비밀번호 페이지에 접근할 수 없습니다");
        }

        noCachePage(response);
        model.addAttribute("postId", postId);
        return VIEW_PREFIX + "guestPostPassword";
    }

    /**
     * 비회원 게시글 수정을 위해서 게시글 비밀번호를 검증합니다.
     * 비밀번호가 일치하면 토큰을 발급하고
     * 게시글 수정 페이지로 redirect 합니다.
     * @param postId
     * @param response
     * @param password
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/{postId}/password")
    public String authenticateGuestPostEdit(@PathVariable Long postId,
                                            HttpServletResponse response,
                                            String password,
                                            RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("id", postId);
        try {
            TokenDto tokenDto = boardService.validatePostPasswordAndGenerateToken(postId, password);
            addTokenCookie(response, tokenDto, postId);
            return "redirect:/board/{postId}/edit";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다");
            return "redirect:/board/{postId}/password";
        } catch (Exception e) {
            log.error("Error 알 수 없는 에러 발생 :: {}", e);
            redirectAttributes.addFlashAttribute("error", "서버 오류로 실패했습니다. 잠시 후 다시 시도해 주세요.");
            return "redirect:/board/{postId}/password";
        }
    }

    @GetMapping("/{postId}/edit")
    public String showEditPostForm(@PathVariable Long postId,
                                 @CookieValue("EditAccessToken") String accessToken,
                                 @CookieValue("EditRefreshToken") String refreshToken,
                                 HttpServletResponse response,
                                 Model model) {
        if(accessToken == null || accessToken.equals("") || refreshToken == null || refreshToken.equals("")) {
            throw new IllegalArgumentException("잘못된 접근입니다");
        }
        boardService.isValidEditAccessToken(postId, accessToken);

        noCachePage(response);
        PostUpdatePageDto dto = boardService.getPostUpdatePageDtoByToken(postId, accessToken);
        model.addAttribute("post", dto);
        return VIEW_PREFIX + "write";
    }

    @PostMapping("/{postId}/edit/tokenRefresh")
    public ResponseEntity refreshEditToken(@PathVariable long postId,
                                           @CookieValue("EditRefreshToken") String refreshToken,
                                           HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("잘못된 접근 입니다");
        }

        TokenDto tokenDto = boardService.generateNewTokensByRefreshToken(postId, refreshToken);
        addTokenCookie(response, tokenDto, postId);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 작성 페이지에서 작성한 게시글을 저장합니다. <br>
     * 게시글 작성에 성공하면 게시글 상세 페이지로 redirect 합니다. <br>
     * 게시글 작성에 실패하면 500 에러를 반환합니다.
     * @param title
     * @param author
     * @param content
     * @param password
     * @param response
     * @return
     */
    @PostMapping("/write")
    public String submitNewPost(String title,
                                String author,
                                String content,
                                String password,
                                HttpServletResponse response) {

        PostGuestWriteDto postGuestWriteDto = new PostGuestWriteDto(title, author, password, content);
        try {
            Post post = boardService.writePost(postGuestWriteDto);
            log.info("save post successfully! :: {}", post.toString());
            return "redirect:/board/"+post.getId();
        } catch (Exception e) {
            log.error("Error. postRepository.save(post) 에서 에러 발생 : {}", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "서버 오류로 글 작성에 실패했습니다. 잠시 후 다시 시도해 주세요.";
        }
    }

    @PostMapping("/{postId}/edit")
    public String submitPostUpdate(@PathVariable long postId,
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
     * sameSite 설정을 통해 CSRF 공격을 방지할 수 있습니다.
     * 게시글 수정용 토큰을 sameSite=strict 로 발급해서, CSRF 공격을 통해 수정 페이지로 이동 자체를 방지합니다.
     * @param response
     * @param tokenDto
     * @param postId
     */
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

    private void noCachePage(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0 호환
        response.setDateHeader("Expires", 0); // 리소스 만료 시점 0 == 1970년 1월 1일 로 설정 => 즉시 만료
    }
}
