package physicks.secondBoard.domain.member;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

// TODO : test 작성 필요
@Component
public class AuthenticationUtils {

    // TODO : Authentication 객체는 구현체에 따라서 email 이 포함되지 않을 수 있음. 그래서 이 부분을 명확히 테스트 해야함. 특히 OAuth2, usernamepassword 둘 다 테스트 필요
    public String extractEmail(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return authentication.getName();
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

            // 이메일 추출 로직. 제공자별로 다를 수 있음
            if (attributes.containsKey("email")) {
                return (String) attributes.get("email");
            } else {
                throw new IllegalArgumentException("OAuth2 Token 의 attribute 에서 email 을 찾을 수 없습니다.");
            }
        }

        throw new IllegalArgumentException("authentication 객체에서 email 을 추출할 수 없습니다.");
    }
}
