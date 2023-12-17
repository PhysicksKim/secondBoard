package physicks.secondBoard.domain.board.dto;

import lombok.Getter;

@Getter
public class PostWriteMemberRequest {

    private String title;
    private String content;

    public PostWriteMemberRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
