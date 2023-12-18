package physicks.secondBoard.web.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.member.MemberRepository;
import physicks.secondBoard.web.controller.request.signup.MemberSignupDto;
import physicks.secondBoard.domain.user.Member;

import java.util.NoSuchElementException;

// TODO : MemberService 분리 필요. { 1. signUp(SignUpController) 2. memberFind(H2UserDetailsService) } 2가지 역할을 수행하고 있음.
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

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("email : " + email + " 인 Member 를 찾을 수 없습니다"));
    }

    public Member findMemberById(Long id) {
        return memberRepository.findMemberById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}
