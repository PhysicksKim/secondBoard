package physicks.secondBoard.domain.member.login;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class FormLoginUser extends User {

    private final String nickname;

    public FormLoginUser(String username, String password, String nickname, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.nickname = nickname;
    }

    public FormLoginUser(String username, String password, String nickname, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(" [");
        sb.append("Username=").append(getUsername()).append(", ");
        sb.append("Password=[PROTECTED], ");
        sb.append("Nickname=").append(getNickname()).append(", ");
        sb.append("Enabled=").append(isEnabled()).append(", ");
        sb.append("AccountNonExpired=").append(isAccountNonExpired()).append(", ");
        sb.append("credentialsNonExpired=").append(isCredentialsNonExpired()).append(", ");
        sb.append("AccountNonLocked=").append(isAccountNonLocked()).append(", ");
        sb.append("Granted Authorities=").append(getAuthorities()).append("]");
        return sb.toString();
    }
}
