package physicks.secondBoard.domain.post.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.author.AuthorRepository;
import physicks.secondBoard.domain.post.PostRepository;

@Service
@RequiredArgsConstructor
public class MemberPostService {

    private final PostRepository postRepository;

    private final AuthorRepository authorRepository;

    // ...
}
