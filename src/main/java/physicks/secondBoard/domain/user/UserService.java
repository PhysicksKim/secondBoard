package physicks.secondBoard.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.member.MemberRepository;
import physicks.secondBoard.exception.UserNotFoundException;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;

    public User findUserById(Long id) throws UserNotFoundException{
        return memberRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User saveMember(Member member) throws UserNotFoundException {
        return memberRepository.save(member);
    }
}
