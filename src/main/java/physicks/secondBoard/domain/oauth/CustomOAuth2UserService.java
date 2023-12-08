package physicks.secondBoard.domain.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import physicks.secondBoard.domain.member.MemberRepository;
import physicks.secondBoard.domain.user.Member;

import java.util.Collections;

// todo : oauth2 service 테스트 작성 필요
// todo : 스프링 시큐리티 테스트 라이브러리를 사용하면 oauth2 유저를 만들 수 있다는데, 알아봐야 할 것 같다.
@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본적으로 제공되는 OAuth2UserService 를 통해서 user 를 가지고 옴. 마치 super.loadUser() 과 유사한 코드
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        log.info("OAuth2 UserRequest : {}", userRequest);
        log.info("OAuth2 User : {}", oAuth2User);

        // 가져온 OAuth2User
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId,
                        userNameAttributeName,
                        oAuth2User.getAttributes());

        log.info("Attributes : {}", attributes.getAttributes());
        saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("OauthUser")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(memberEntity -> memberEntity.updateName(attributes.getName()))
                .orElse(attributes.toEntity());
        return memberRepository.save(member);
    }
}