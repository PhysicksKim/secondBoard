package physicks.secondBoard.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {
    public String getUserName(Authentication authentication) {
        if(authentication == null) {
            return "Guest";
        }

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        return (String) principal.getAttributes().get("name");
    }
}