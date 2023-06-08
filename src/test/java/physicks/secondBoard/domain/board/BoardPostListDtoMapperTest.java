package physicks.secondBoard.domain.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.post.Post;
import physicks.secondBoard.domain.post.PostRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 게시판에서 Post의 작성시간을 표시 할 때, 오늘 작성된 게시글은 "시:분" 으로 표기하고, 아닌 경우 "월/일"로 표기한다 <br>
 * 이를 구분하여 DtoMappingTest를 수행한다. <br>
 */
@SpringBootTest
@Transactional
class BoardPostListDtoMapperTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    private BoardPostListDtoMapper boardPostListDtoMapper;

    @Test
    public void PostToDtoMappingTest_today인경우() throws Exception {
        //given
        Post post = Post.of("title","author","content");
        Post savedPost = postRepository.save(post);

        //when
        BoardPostListDto dto = boardPostListDtoMapper.toDto(savedPost);

        //then
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Assertions.assertThat(dto.getId()).isEqualTo(savedPost.getId());
        Assertions.assertThat(dto.getTitle()).isEqualTo(savedPost.getTitle());
        Assertions.assertThat(dto.getAuthor()).isEqualTo(savedPost.getAuthor());
        Assertions.assertThat(dto.getCreatedTime()).isEqualTo(savedPost.getCreatedTime().format(formatter));
    }

    @Test
    public void PostToDtoMappingTest_today아닌경우() throws Exception {
        //given
        Post post = Post.of("title","author","content");
        Post savedPost = postRepository.save(post);

        // reflection을 사용하여 어제 작성된 글로 만든다
        LocalDateTime yesterday = LocalDateTime.now().minusDays(2);
        ReflectionTestUtils.setField(savedPost, "createdTime", yesterday, LocalDateTime.class);

        //when
        BoardPostListDto dto = boardPostListDtoMapper.toDto(savedPost);

        //then
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        Assertions.assertThat(dto.getId()).isEqualTo(savedPost.getId());
        Assertions.assertThat(dto.getTitle()).isEqualTo(savedPost.getTitle());
        Assertions.assertThat(dto.getAuthor()).isEqualTo(savedPost.getAuthor());
        Assertions.assertThat(dto.getCreatedTime()).isEqualTo(savedPost.getCreatedTime().format(formatter));
    }
}