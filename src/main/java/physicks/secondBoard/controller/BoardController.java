package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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
}
