package physicks.secondBoard.domain.board;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * <h3>용도</h3>
 * 게시글 관련 인증 로직을 담음.
 * 회원 인증 관련 로직은 {@link physicks.secondBoard.domain.user.AuthService} 참고.
 *
 * <h3>설명</h3>
 * 비회원 글인 경우 게시글 수정 시 정상적인 경로를 따라 수정 요청을 보냈는지 체크해야한다.
 * 정상적인 경로는 1. 수정 버튼 클릭 2. 게시글 비밀번호 입력(인증과정) 3. 게시글 수정 후 submit
 * 위와 같이 3가지 과정을 따라야 한다.
 * 하지만 공격자가 3.의 Request 양식을 알고
 * 인증 과정인 2.를 건너뛰어서 form request를 흉내내서 보내면
 * 게시글이 인증없이도 수정될 수 있다.
 * 따라서 2 인증과정에서 JWT 토큰을 발급하고, 3의 submit 과정에서 jwt 토큰을 검증하는 식으로 동작해야 한다
 * </pre>
 */
@Service
@Slf4j
public class BoardAuthenticationService {

    /**
     * 게시글 수정 권한이 있는지 JWT 토큰 기반으로 검증함.
     */
    public boolean isValidUpdateJWT(Long pathId, Long id) {

        // ===================================
        // !!! 차후 JWT 기반 검증으로 변경 필요 !!!
        // ===================================

        if(pathId != id) {
            // return "redirect:/";
            return false;
        }

        return true;
    }
}
