package physicks.secondBoard.domain.board.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * <pre>
 * 글 작성 Request 를 처리하는 DTO 입니다
 * Request -> Controller -> BoardService -> PostService 까지 전달됩니다
 * </pre>
 */
@Getter
public class PostWriteGuestDto {

    @NotNull
    @Size(min = 6)
    private String title;

    @Size
    private String authorName;
    private String password;
    private String content;

    public PostWriteGuestDto(@NotNull String title, @NotNull String authorName, @NotNull String password, @NotNull String content) {
        this.title = title;
        this.authorName = authorName;
        this.password = password;
        this.content = content;
    }

    @Override
    public String toString() {
        return "PostWriteGuestDto{" +
                "title='" + title + '\'' +
                ", author='" + authorName + '\'' +
                ", password='" + password + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
