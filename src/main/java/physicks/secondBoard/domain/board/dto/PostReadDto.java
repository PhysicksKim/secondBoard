package physicks.secondBoard.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostReadDto {
    private Long id; // Post.id
    private String title; // Post.title
    private String authorName; // 비회원 : Post.author.name 회원 : Post.author.user.name
    private String content; // Post.content
    private String createdTime; // Post.createdTime
    private boolean isGuest; // Post.author.isGuest
}
