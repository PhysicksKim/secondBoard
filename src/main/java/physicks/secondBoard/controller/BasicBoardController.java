package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import physicks.secondBoard.domain.board.BoardPostListDto;
import physicks.secondBoard.domain.board.BoardService;
import physicks.secondBoard.domain.post.Post;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BasicBoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    public String boardMain(Model model, Pageable pageable) {
        List<BoardPostListDto> postList = boardService.getBoardPostList(pageable);
        model.addAttribute("postList", postList);
        return "board";
    }

    @GetMapping("/board/{id}")
    public String boardPostRead(@PathVariable Long id, Model model) {
        Post findPost = boardService.findPostById(id);
        model.addAttribute("post", findPost);
        return "post";
    }

    @PostMapping("/board")
    @ResponseBody
    public String writePost(String title, String author, String content) {
        Post post = new Post(title, author, content);

        try {
            boardService.savePost(post);
        } catch (Exception e) {
            log.error("Error. postRepository.save(post) 에서 에러 발생 : {}", e);
        }
        log.info("success save post");
        return "success save post";
    }
}
