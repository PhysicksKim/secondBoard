package physicks.secondBoard.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationUtilsTest {

    private final AuthenticationUtils authenticationUtils = new AuthenticationUtils();

    @DisplayName("UsernamePasswordAuthenticationToken 에서 email 추출")
    @Test
    void extractEmailFromUsernamePasswordAuthenticationToken() {
        String expectedEmail = "user@example.com";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(expectedEmail, "password");

        String email = authenticationUtils.extractEmail(token);

        assertEquals(expectedEmail, email);
    }

    @DisplayName("OAuth2AuthenticationToken 에서 email 추출")
    @Test
    void extractEmailFromOAuth2AuthenticationToken() {
        String expectedEmail = "user@example.com";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", expectedEmail);

        OAuth2UserAuthority authority = new OAuth2UserAuthority(attributes);
        DefaultOAuth2User user = new DefaultOAuth2User(Collections.singletonList(authority), attributes, "email");
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(user, Collections.emptyList(), "client");

        String email = authenticationUtils.extractEmail(token);

        assertEquals(expectedEmail, email);
    }

    @DisplayName("OAuth2AuthenticationToken 에서 email 추출 시, email 이 없으면 예외 발생")
    @Test
    void extractEmailShouldThrowExceptionIfEmailNotFound() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("loginId", "testerKim"); // 유효하지만 이메일이 없는 속성 추가

        OAuth2UserAuthority authority = new OAuth2UserAuthority(attributes);
        DefaultOAuth2User user = new DefaultOAuth2User(Collections.singletonList(authority), attributes, "loginId");
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(user, Collections.emptyList(), "client");

        assertThrows(IllegalArgumentException.class, () -> authenticationUtils.extractEmail(token));
    }
}