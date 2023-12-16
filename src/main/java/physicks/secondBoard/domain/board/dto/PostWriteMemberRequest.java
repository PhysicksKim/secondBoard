package physicks.secondBoard.domain.board.dto;

import lombok.Getter;

@Getter
public class PostWriteMemberRequest {

    private String title;
    private String password;
    private String content;

    public PostWriteMemberRequest(String title, String password, String content) {
        this.title = title;
        this.password = password;
        this.content = content;
    }

}
