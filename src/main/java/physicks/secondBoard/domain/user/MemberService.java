package physicks.secondBoard.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import physicks.secondBoard.exception.UserNotFoundException;

@Service
@Getter
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /*
    회원가입, 로그인 기능
     */

    /**
     * 회원가입
     * @param dto {@link MemberRegisterDto}
     * @return memberId
     */
    public Long registerMember(MemberRegisterDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = Member.of(dto.getLoginId(),
                encodedPassword,
                dto.getNickname(),
                dto.getEmail()
        );

        // --- validation ---
        // ------------------

        // 검증 과정 통과
        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    public Long login(MemberLoginDto dto) {
        // find
        Member member = memberRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new UsernameNotFoundException("해당 Login id 를 찾을 수 없습니다"));

        // validation
        if (passwordEncoder.matches(dto.getRawPassword(), member.getPassword())) {
            return member.getId();
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }

    public Member findMemberById(Long savedId) throws UserNotFoundException{
        return memberRepository.findMemberById(savedId).orElseThrow(UserNotFoundException::new);
    }

    // 회원 정보 변경. 닉네임
    public void updateNickname(Long id, String nickname) throws UserNotFoundException{
        Member memberById = findMemberById(id);
        memberById.updateNickname(nickname);
    }

    // soft delete
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
