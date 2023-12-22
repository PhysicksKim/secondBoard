package physicks.secondBoard.web.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.user.Member;
import physicks.secondBoard.web.controller.request.signup.MemberSignupDto;

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

    @DisplayName("회원 가입 후 비밀번호는 인코딩 되어야 한다.")
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

}