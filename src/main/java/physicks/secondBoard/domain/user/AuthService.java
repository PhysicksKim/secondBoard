package physicks.secondBoard.domain.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public String getUserName(Authentication authentication) {
        if(authentication == null) {
            return "Guest";
        }

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        return (String) principal.getAttributes().get("name");
    }
}