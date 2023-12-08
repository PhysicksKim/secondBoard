package physicks.secondBoard.domain.member.login;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import physicks.secondBoard.web.controller.request.signup.MemberSignupDto;
import physicks.secondBoard.domain.user.Member;
import physicks.secondBoard.web.service.MemberService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class H2UserDetailsServiceTest {

    @Autowired
    private H2UserDetailsService h2UserDetailsService;

    @Autowired
    private MemberService memberService;

    @DisplayName("")
    @Test
    void loadUserByUsername() {
        // given
        String email = "tester@test.com";

        MemberSignupDto dto = new MemberSignupDto(email,"Password1!","tester");
        Long memberId = memberService.signupMember(dto);
        Member findMember = memberService.findMemberById(memberId);

        // when
        UserDetails tester = h2UserDetailsService.loadUserByUsername(email);

        // then
        assertThat(tester.getUsername()).isEqualTo(findMember.getEmail());
        assertThat(tester.getPassword()).isEqualTo(findMember.getPassword());
    }

}