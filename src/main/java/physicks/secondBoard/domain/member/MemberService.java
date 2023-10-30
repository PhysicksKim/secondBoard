package physicks.secondBoard.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.member.signup.MemberRegisterDto;
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
     * @param dto {@link MemberRegisterDto}
     * @return memberId
     */
    public Long registerMember(MemberRegisterDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member savedMember = memberRepository.save(Member
                .of(encodedPassword, dto.getName(), dto.getEmail(), false));
        return savedMember.getId();
    }

    /**
     * 로그인
     * @param dto
     * @return
     */
    public Long login(MemberLoginDto dto) {
        // find
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Login id 를 찾을 수 없습니다"));

        // validation
        if (passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            return member.getId();
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }

    public Member findMemberById(Long savedId) throws UserNotFoundException{
        return memberRepository.findMemberById(savedId).orElseThrow(UserNotFoundException::new);
    }
}
