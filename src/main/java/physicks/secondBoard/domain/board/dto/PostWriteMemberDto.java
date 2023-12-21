package physicks.secondBoard.domain.board.dto;

import lombok.Getter;

@Getter
public class PostWriteMemberDto {

    private String title;
    private String content;

    public PostWriteMemberDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
