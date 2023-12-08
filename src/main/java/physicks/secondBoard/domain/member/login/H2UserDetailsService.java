package physicks.secondBoard.domain.member.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import physicks.secondBoard.web.service.MemberService;
import physicks.secondBoard.domain.user.Member;

import java.util.Collections;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class H2UserDetailsService implements UserDetailsService {

    // Service 는 Service 를 의존하는 게 좋습니다.
    // repository 를 의존하면, 비즈니스 로직을 거치지 않을 위험이 있습니다.
    private final MemberService memberService;

    /**
     * UserDetailsService Interface 에서 말하는 Username 은 회원의 고유한 식별자를 말합니다.
     * username 은 email 로 사용합니다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("login email : {}", email);
        Member findMember = memberService.findMemberByEmail(email);
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return new FormLoginUser(findMember.getEmail(), findMember.getPassword(), findMember.getName(), authorities);
    }
}
