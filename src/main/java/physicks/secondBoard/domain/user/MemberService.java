package physicks.secondBoard.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;

    /*
    회원가입, 로그인 기능
     */

    /**
     * 회원가입
     * @param dto {@link MemberRegisterDto}
     * @return memberId
     */
    public Long registerMember(MemberRegisterDto dto) {
        Member member = Member.of(dto.getLoginId(),
                dto.getPassword(),
                dto.getNickname(),
                dto.getEmail()
        );

        // --- validation ---
        // --- --- ---

        // 검증 과정 통과
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    public Long login(MemberLoginDto dto) {
        // find

        // if null
        //   로그인 실패 :

        //   로그인 성공 :

        return -1L;
    }
}
