package physicks.secondBoard.domain.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("author 를 저장하고, findAll 로 가져옵니다")
    @Test
    void saveAndFind() {
        // given
        Author savedAuthor = authorRepository.save(Author.ofGuest("name", "password"));

        // when
        Author findAuthor = authorRepository.findAll().get(0);

        // then
        assertThat(findAuthor).isNotNull();
        assertThat(findAuthor.getId()).isEqualTo(savedAuthor.getId());
        assertThat(findAuthor).isEqualTo(savedAuthor);
    }

    @DisplayName("author 를 저장하고, findById 로 가져옵니다")
    @Test
    void findById() {
        // given
        Author savedAuthor = authorRepository.save(Author.ofGuest("name", "password"));

        // when
        Author findAuthor = authorRepository.findById(savedAuthor.getId()).orElseThrow();

        // then
        assertThat(findAuthor).isNotNull();
        assertThat(findAuthor.getId()).isEqualTo(savedAuthor.getId());
        assertThat(findAuthor).isEqualTo(savedAuthor);
    }

}