package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import physicks.secondBoard.domain.boardList.PostListDto;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BasicBoardController {

    private final PostRepository postRepository;

    @GetMapping("/board")
    public String boardMain(Model model) {

        PostListDto dto1 = PostListDto.builder()
                .id(123L)
                .title("TEST title")
                .author("TEST author")
                .createdTime(LocalDateTime.now())
                .build();
        PostListDto dto2 = PostListDto.builder()
                .id(124L)
                .title("TEST title2")
                .author("TEST author2")
                .createdTime(LocalDateTime.now())
                .build();

        List<PostListDto> postList = new ArrayList<>();
        postList.add(dto1);
        postList.add(dto2);

        model.addAttribute("postList", postList);

        return "board";
    }

    @PostMapping("/board")
    @ResponseBody
    public String writePost(String title, String author, String content) {
        LocalDateTime now = LocalDateTime.now();
        Post post = Post.builder()
                .title(title)
                .author(author)
                .content(content)
                .build();

        try {
            postRepository.save(post);
        } catch (Exception e) {
            log.error("Error. postRepository.save(post) 에서 에러 발생 : {}", e);
        }

        log.info("success save post");

        return "success save post";
    }

}
