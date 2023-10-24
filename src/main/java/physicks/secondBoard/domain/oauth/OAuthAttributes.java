package physicks.secondBoard.domain.oauth;

import lombok.Builder;
import lombok.Getter;
import physicks.secondBoard.domain.user.Member;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    /**
     *
     * @param registrationId google 과 같이 OAuth2 provider 의 이름이 담겨있음 (ex.google)
     * @param userNameAttributeName attribute 에서 username 부분이 어디에 있는지 key string 을 알려줌 (ex.sub)
     * @param attributes 이메일, 이름, 프로필 사진 등 정보들이 담긴 Map
     * @return
     */
    public  static OAuthAttributes of(String registrationId,
                                      String userNameAttributeName,
                                      Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.ofOauth(name, email);
        /*
        // OAuth Picture 값을 처리하는 경우
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
                */
    }
}