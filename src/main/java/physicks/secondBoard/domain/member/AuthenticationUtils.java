package physicks.secondBoard.domain.member;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthenticationUtils {

    public String extractEmail(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return authentication.getName(); // UsernamePasswordAuthenticationToken 은 email 을 name 으로 저장하도록 설정해뒀음
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            // OAuth2 인증에서 email 추출 방식은, OAuth2 제공자마다 구현이 다름
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

            // 제공자별로 다른 이메일 추출 로직
            if (attributes.containsKey("email")) {
                return (String) attributes.get("email");
            } else {
                throw new IllegalArgumentException("OAuth2 Token 의 attribute 에서 email 을 찾을 수 없습니다.");
            }
        }

        throw new IllegalArgumentException("authentication 객체에서 email 을 추출할 수 없습니다.");
    }
}
