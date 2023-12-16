package physicks.secondBoard.domain.board.dto;


import lombok.*;

/**
 * <pre>
 * 글 작성 Request 를 처리하는 DTO 입니다
 * Request -> Controller -> BoardService -> PostService 까지 전달됩니다
 * </pre>
 */
@Getter
public class PostWriteGuestRequest {
    private String title;
    private String authorName;
    private String password;
    private String content;

    public PostWriteGuestRequest(String title, String authorName, String password, String content) {
        this.title = title;
        this.authorName = authorName;
        this.password = password;
        this.content = content;
    }

    @Override
    public String toString() {
        return "PostWriteGuestRequest{" +
                "title='" + title + '\'' +
                ", author='" + authorName + '\'' +
                ", password='" + password + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
