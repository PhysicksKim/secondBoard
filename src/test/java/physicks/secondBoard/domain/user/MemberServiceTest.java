package physicks.secondBoard.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.member.MemberService;
import physicks.secondBoard.domain.member.signup.MemberSignupDto;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    private final static String NAME_PREFIX = "sampleMember";
    private final static String EMAIL_SUFFIX = "@test.com";
    private final static String PASSWORD_PREFIX = "password";


    /**
     * Member Signup 과정에 사용되는 MemberSignupDto 는 rawPassword 를 담고 있습니다.
     * password Encoding 은 MemberService 내부에서 이뤄져서 DTO가 Member Entity로 변환됩니다.
     */
    @BeforeEach
    void addSampleMembers() {
        final int numOfMembers = 10;
        for(int i = 0 ; i < numOfMembers ; i++) {
            MemberSignupDto dto = new MemberSignupDto(
                    NAME_PREFIX + i + EMAIL_SUFFIX,
                    PASSWORD_PREFIX + i,
                    NAME_PREFIX + i
            );
            memberService.signupMember(dto);
        }
    }

    @Test
    void 패스워드인코더_테스트() {
        final String rawHello = "hello";
        String encodedHello = passwordEncoder.encode(rawHello);

        log.info("rawHello = {} , encodedHello = {}", rawHello, encodedHello);
        assertThat(rawHello).isNotEqualTo(encodedHello);
        Assertions.assertTrue(passwordEncoder.matches(rawHello, encodedHello));
    }

    @Test
    void 회원가입() {
        // given
        final String email = "physicks@board.com";
        final String rawPassword = "kim1357!";
        final String name = "physicks";

        // when
        Long savedId = memberService.signupMember(
                new MemberSignupDto(email, rawPassword, name)
        );
        Member member = memberService.findMemberById(savedId);
        log.info("saved member password : {}", member.getPassword());

        // then
        assertThat(passwordEncoder.encode(rawPassword)).isNotEqualTo(member.getPassword());
        Assertions.assertTrue(passwordEncoder.matches(rawPassword, member.getPassword()));
    }
    //
    // @Test
    // void 로그인_성공() {
    //     // given
    //     final String email = NAME_PREFIX+1+EMAIL_SUFFIX;
    //     final String rawPassword = PASSWORD_PREFIX+1;
    //     MemberLoginDto dto = new MemberLoginDto(email, rawPassword);
    //
    //     // when
    //     Long memberId = memberService.login(dto);
    //     Member loginMember = memberService.findMemberById(memberId);
    //
    //     // then
    //     assertThat(loginMember.getPassword()).isNotEqualTo(rawPassword);
    //     Assertions.assertTrue(passwordEncoder.matches(rawPassword, loginMember.getPassword()));
    //     assertThat(loginMember.getName()).isEqualTo(NAME_PREFIX+1);
    //     assertThat(loginMember.getEmail()).isEqualTo(NAME_PREFIX+1+EMAIL_SUFFIX);
    //     assertThat(loginMember.getRole()).isEqualTo(Role.MEMBER);
    // }
    //
    // @Test
    // void 로그인_실패() {
    //     // given
    //     final String email = NAME_PREFIX + 1 + EMAIL_SUFFIX;
    //     final String rawPassword = "wrongPassword";
    //     MemberLoginDto dto = new MemberLoginDto(email, rawPassword);
    //
    //     // when && then
    //     Assertions.assertThrows(BadCredentialsException.class, () -> memberService.login(dto));
    // }
}