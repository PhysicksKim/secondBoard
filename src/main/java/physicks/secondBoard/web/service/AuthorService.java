package physicks.secondBoard.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.post.author.Author;
import physicks.secondBoard.domain.post.author.AuthorRepository;
import physicks.secondBoard.domain.user.Member;

// todo : test 작성 필요
/**
 * Post 의 Author 를 생성하는 서비스입니다.
 * Author 생성은 AuthorService 를 통해서 이뤄져야 합니다.
 * Author 의 password 를 encoded 상태로 무결성을 보장하기 위해 AuthorService 를 별도로 분리했습니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    public Author createGuestAuthor(String authorName, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Author author = Author.ofGuest(authorName, encodedPassword);
        return authorRepository.save(author);
    }

    public Author createMemberAuthor(Member member) {
        Author author = Author.ofMember(member);
        return authorRepository.save(author);
    }

    public boolean isMatchedPassword(Author author, String rawPassword) {
        return passwordEncoder.matches(rawPassword, author.getPassword());
    }

}
