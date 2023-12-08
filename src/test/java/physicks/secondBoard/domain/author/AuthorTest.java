package physicks.secondBoard.domain.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import physicks.secondBoard.domain.user.Member;
import physicks.secondBoard.exception.EntityConstraintViolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorTest {

    @DisplayName("Author 의 회원/비회원 제약조건에서, 비회원인 경우 user 필드가 null 이여야 합니다")
    @Test
    void checkConstraints_guestTrueMemberNotNull() {
        // given
        Author author = new Author();
        author.isGuest = true;
        author.user = Member.of("encodedPassword","name", "email@test.com", false);

        // when / then
        assertThrows(EntityConstraintViolation.class, author::checkConstraints);
    }

    @DisplayName("Author 의 회원/비회원 제약조건에서, 회원인 경우 user 필드가 not null 이여야 합니다")
    @Test
    void checkConstraints_guestFalseMemberNull() {
        // given
        Author author = new Author();
        author.isGuest = false;
        author.user = null;

        // when / then
        assertThrows(EntityConstraintViolation.class, author::checkConstraints);
    }

    @DisplayName("ofGuest 생성 메서드는 Guest Author 를 생성합니다")
    @Test
    void ofGuest_isGuest() {
        // given / when
        Author guest = Author.ofGuest("name", "password");

        // then
        assertThat(guest.isGuest()).isTrue();
        assertThat(guest.isGuest).isTrue();
        assertThat(guest.user).isNull();
    }

    @DisplayName("ofGuest 생성 메서드로 fields 값을 채워넣습니다")
    @Test
    void ofGuest_fields() {
        // given
        String name = "name";
        String password = "123456aA!";

        // when
        Author guest = Author.ofGuest(name, password);

        // then
        assertThat(guest.name).isEqualTo(name);
        assertThat(guest.password).isEqualTo(password);
    }

    @DisplayName("ofMember 생성 메서드는 ")
    @Test
    void ofMember_isGuestForMember() {
        // given
        String name = "name";
        String encodedPassword = "encodedPassword";
        String email = "tester@test1234.com";
        boolean isOauthUser = false;

        Member member = Member.of(encodedPassword, name, email, isOauthUser);

        // when
        Author author = Author.ofMember(member);

        // then
        assertThat(author.isGuest()).isFalse();
        assertThat(author.isGuest).isFalse();
        assertThat(author.user).isEqualTo(member);
    }

    @DisplayName("ofMember 생성 메서드로 fields 값을 채워넣습니다")
    @Test
    void ofMember_fields() {
        // given
        String name = "name";
        String encodedPassword = "encodedPassword";
        String email = "tester@test1234.com";
        boolean isOauthUser = false;

        Member member = Member.of(encodedPassword, name, email, isOauthUser);

        // when
        Author author = Author.ofMember(member);

        // then
        assertThat(author.name).isEqualTo(name);
        assertThat(author.password).isNull();
    }
}