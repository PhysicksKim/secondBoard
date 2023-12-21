package physicks.secondBoard.web.controller.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import physicks.secondBoard.validator.ValidPassword;

@Getter
public class PostWriteRequest {

    @NotNull
    @Size(min = 6)
    private String title;

    @NotBlank
    @Size(min = 10)
    private String content;

    @Nullable
    @Size(min = 2, max = 10)
    private String authorName;

    @Nullable
    @ValidPassword
    private String password;

    public PostWriteRequest(String title, String content, @Nullable String authorName, @Nullable String password) {
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "PostWriteRequest{" +
                "title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", password='" + password + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
