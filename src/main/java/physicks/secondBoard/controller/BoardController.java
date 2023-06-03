package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import physicks.secondBoard.domain.board.BoardPostListDto;
import physicks.secondBoard.domain.board.BoardService;
import physicks.secondBoard.domain.post.Post;

import java.util.List;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String boardMain(Model model, Pageable pageable) {
        List<BoardPostListDto> postList = boardService.getBoardPostList(pageable);
        model.addAttribute("postList", postList);
        return "board";
    }

    @GetMapping("/{id}")
    public String boardPostRead(@PathVariable Long id, Model model) {
        Post findPost = boardService.findPostById(id);
        model.addAttribute("post", findPost);
        return "post";
    }

    @GetMapping("/write")
    public String boardPostWritePage() {
        // !!! 구현 필요 !!!
        // 1. Post Write Test 먼저 구현
        // 2. Post Write 로직과 Page 구현 필요
        return "write";
    }

    @PostMapping("/write")
    @ResponseBody
    public String writePost(String title, String author, String content) {
        Post post = new Post(title, author, content);
        System.out.println("post.toString() = " + post.toString());

        try {
            boardService.savePost(post);
        } catch (Exception e) {
            log.error("Error. postRepository.save(post) 에서 에러 발생 : {}", e);
        }
        log.info("success save post");
        return "success save post";
    }
}
