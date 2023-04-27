package physicks.secondBoard.domain.board;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostListDto {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdTime;

}


