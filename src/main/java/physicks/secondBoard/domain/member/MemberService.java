package physicks.secondBoard.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.member.signup.MemberSignupDto;
import physicks.secondBoard.domain.user.Member;
import physicks.secondBoard.exception.UserNotFoundException;

@Service
@Getter
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param dto {@link MemberSignupDto}
     * @return memberId
     */
    public Long signupMember(MemberSignupDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member savedMember = memberRepository.save(Member
                .of(encodedPassword, dto.getName(), dto.getEmail(), false));
        return savedMember.getId();
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 email 을 찾을 수 없습니다"));
    }

    /**
     * @Deprecated 로그인 요청이 Security Chain 에 의해서 이뤄지므로 해당 메서드는 사용하지 않음
     * @param dto
     * @return
     */
    @Deprecated
    public Long loginValidate(MemberLoginDto dto) {
        // find
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Login id 를 찾을 수 없습니다"));

        // validation
        if (passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            return member.getId();
        } else {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }
    }

    public Member findMemberById(Long savedId) throws UserNotFoundException{
        return memberRepository.findMemberById(savedId).orElseThrow(UserNotFoundException::new);
    }
}
