package physicks.secondBoard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BasicBoardController {

    private final PostRepository postRepository;

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
