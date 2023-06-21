package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import physicks.secondBoard.config.oauth.CustomOAuth2UserService;
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
        // - 검증 개선 방향
        // 어차피 postman 같은거로 /write/11 에서 보낸 것 처럼 출발 url 꾸미고
        // form data도 다 위처럼 넣으면 이정도 필터링은 바로 뚫린다.
        // 그래서 결국 author 유효한지 검증이 더 중요하다.

        // !!! 수정 필요 !!!
        // bindingResult 로 validation 구현 필요
        if(pathId != id) {
            return "redirect:/";
        }

        boardService.updatePost(id, title, author, content);
        return "redirect:/board/" + id;
    }
}
