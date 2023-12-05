package physicks.secondBoard.web.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostUpdateRequestDto {
    private final long id;
    private final String title;
    private final String author;
    private final String content;
}
