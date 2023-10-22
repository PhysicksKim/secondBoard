package physicks.secondBoard.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.exception.UserNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void addSampleMembers() {
        final int numOfMembers = 10;
        for(int i = 0 ; i < numOfMembers ; i++) {
            MemberRegisterDto dto = new MemberRegisterDto(
                    "sampleMember" + i,
                    "password" + i,
                    "sampleNickname" + i,
                    "member" + i + "@physicks.com"
            );

            memberService.registerMember(dto);
        }
    }

    @Test
    void 패스워드인코더_테스트() {
        final String rawHello = "hello";
        String encodedHello = passwordEncoder.encode(rawHello);

        log.info("rawHello = {} , encodedHello = {}", rawHello, encodedHello);
        assertThat(rawHello).isNotEqualTo(encodedHello);
    }

    @Test
    void 회원가입() throws UserNotFoundException {
        // given
        final String loginId = "kim";
        final String rawPassword = "kim1357!";
        final String nickname = "physicks";
        final String email = "physicks@board.com";

        // when
        Long savedId = memberService.registerMember(
                new MemberRegisterDto(loginId, rawPassword, nickname, email)
        );
        Member member = memberService.findMemberById(savedId);
        log.info("saved member password : {}", member.getPassword());

        // then
        assertThat(passwordEncoder.encode(rawPassword)).isNotEqualTo(member.getPassword());
        Assertions.assertTrue(passwordEncoder.matches(rawPassword, member.getPassword()));
    }

    @Test
    void 로그인_성공() throws UserNotFoundException{
        // given
        final String loginId = "sampleMember1";
        final String rawPassword = "password1";
        MemberLoginDto dto = new MemberLoginDto(loginId, rawPassword);

        // when
        Long memberId = memberService.login(dto);
        Member loginMember = memberService.findMemberById(memberId);

        // then
        assertThat(loginMember.getPassword()).isNotEqualTo(rawPassword);
        Assertions.assertTrue(passwordEncoder.matches(rawPassword, loginMember.getPassword()));
        assertThat(loginMember.getName()).isEqualTo("sampleNickname1");
        assertThat(loginMember.getEmail()).isEqualTo("member1@physicks.com");
        assertThat(loginMember.getRole()).isEqualTo(Role.MEMBER);
    }

    @Test
    void 로그인_실패() {
        // given
        final String loginId = "sampleMember1";
        final String rawPassword = "wrongPassword";
        MemberLoginDto dto = new MemberLoginDto(loginId, rawPassword);

        // when && then
        Assertions.assertThrows(BadCredentialsException.class, () -> memberService.login(dto));
    }
}