package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import physicks.secondBoard.domain.board.BoardAuthenticationService;
import physicks.secondBoard.domain.board.BoardPostListDto;
import physicks.secondBoard.domain.board.BoardService;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.user.AuthService;

import java.util.List;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    // 게시글 관련 인증로직
    private final BoardAuthenticationService boardAuthenticationService;

    private final BoardService boardService;

    private final AuthService authService;

    @GetMapping
    public String mainPage(Model model, Pageable pageable, Authentication authentication) {
        String userName = authService.getUserName(authentication);
        log.info("userName : {}", userName);

        List<BoardPostListDto> postList = boardService.getBoardPostList(pageable);
        model.addAttribute("postList", postList);
        return "board";
    }

    @GetMapping("/{id}")
    public String postRead(@PathVariable Long id, Model model) {
        Post findPost = boardService.findPostById(id);
        model.addAttribute("post", findPost);
        return "post";
    }

    @GetMapping("/write")
    public String postWritePage() {
        return "write";
    }

    @GetMapping("/write/{id}")
    public String postUpdatePage(@PathVariable Long id, Model model) {
        Post findPost = boardService.findPostById(id);

        model.addAttribute("post", findPost);
        return "write";
    }


    /**
     * from message 예시 <br>
     * @ResponseBody => title=testTitle&author=testAuthor&content=testContent
     */
    @PostMapping("/write")
    public String writePost(String title, String author, String content) {
        Post post = Post.of(title, author, content);
        System.out.println("post.toString() = " + post);

        try {
            boardService.savePost(post);
        } catch (Exception e) {
            log.error("Error. postRepository.save(post) 에서 에러 발생 : {}", e);
        }
        log.info("save post successfully! : id = {}", post.getId());

        return "redirect:/board/"+post.getId();
    }

    @PostMapping("/write/{pathId}")
    public String updatePost(@PathVariable Long pathId,
                             Long id, String title, String author, String content) {

        // !!! 수정 필요 !!!
        // - 일단은 간단하게 검증 함
        // 만약 post id 가 form hidden 에 없이 url만 날아온 경우
        // 잘못된 접근으로 판단해서 그냥 home으로 redirect 한다
        //
        // - 검증 개선 방향
        // 어차피 postman 같은거로 /write/11 에서 보낸 것 처럼 출발 url 꾸미고
        // form data도 다 위처럼 넣으면 이정도 필터링은 바로 뚫린다.
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


        boardService.updatePost(id, title, author, content);
        return "redirect:/board/" + id;
    }
}
