package physicks.secondBoard.domain.oauth;

import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.member.MemberRepository;
import physicks.secondBoard.domain.user.Member;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 *
 * 참고 : <a href="https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-client/src/test/java/org/springframework/security/oauth2/client/userinfo/DefaultOAuth2UserServiceTests.java">Spring Security Github Test Code</a>
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomOAuth2UserServiceTest {

    @Autowired
    private CustomOAuth2UserService userService;

    @Autowired
    private MemberRepository memberRepository;

    private MockWebServer server;

    private OAuth2AccessToken accessToken;

    final String DUMMY_URI = "http://DummyUri.com/dummy";

    final String userNameAttributeName = "user-name";

    @BeforeEach
    public void setup() throws Exception {
        this.server = new MockWebServer();
        this.server.start();
        Instant issuedAt = Instant.now();
        Instant expiresAt = Instant.now().plusSeconds(60L);
        this.accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "token1234", issuedAt, expiresAt);
    }

    @AfterEach
    public void cleanup() throws Exception {
        this.server.shutdown();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("OAuth 이메일과 일치하는 유저가 없는 경우 새로운 유저를 생성한다")
    @Transactional
    @Test
    public void loadUser_SuccessAndSaveNewOAuthMember() {
        // given
        String userInfoResponse = "{\n"
                + "   \"name\": \"user1\",\n"
                + "   \""+userNameAttributeName+"\": \"user1\",\n"
                + "   \"first-name\": \"first\",\n"
                + "   \"last-name\": \"last\",\n"
                + "   \"middle-name\": \"middle\",\n"
                + "   \"address\": \"address\",\n"
                + "   \"email\": \"user1@example.com\"\n"
                + "}\n";
        this.server.enqueue(jsonResponse(userInfoResponse));

        String userInfoUri = this.server.url("/user").toString();

        ClientRegistration clientRegistration = getClientRegistration(userInfoUri);

        // when
        OAuth2User user = this.userService.loadUser(new OAuth2UserRequest(clientRegistration, this.accessToken));
        log.info("OAuth2User : {}", user);

        // then
        assertThat(user).isNotNull();
        assertThat((String)user.getAttribute("user-name")).isEqualTo("user1");
        assertThat((String)user.getAttribute("first-name")).isEqualTo("first");
        assertThat((String)user.getAttribute("last-name")).isEqualTo("last");
        assertThat((String)user.getAttribute("middle-name")).isEqualTo("middle");
        assertThat((String)user.getAttribute("address")).isEqualTo("address");
        assertThat((String)user.getAttribute("email")).isEqualTo("user1@example.com");
        assertThat(user.getAuthorities().isEmpty()).isFalse();
        assertThat(user.getAuthorities().size()).isOne();

        List<Member> afterMembers = memberRepository.findAll();
        assertThat(afterMembers.size()).isOne();
    }

    @DisplayName("OAuth 이메일과 일치하는 유저가 있는 경우 해당 유저로 로그인 한다")
    @Transactional
    @Test
    public void loadUser_SuccessAndLoginWithOAuthEmail() {
        // given
        final String oauthEmail = "user1@example.com";
        Member savedMember = Member.ofOauth("user1", oauthEmail);
        memberRepository.save(savedMember);
        log.info("savedMember : {}", savedMember);

        String userInfoResponse = "{\n"
                + "   \"name\": \"user1\",\n"
                + "   \""+userNameAttributeName+"\": \"user1\",\n"
                + "   \"first-name\": \"first\",\n"
                + "   \"last-name\": \"last\",\n"
                + "   \"middle-name\": \"middle\",\n"
                + "   \"address\": \"address\",\n"
                + "   \"email\": \"user1@example.com\"\n"
                + "}\n";
        this.server.enqueue(jsonResponse(userInfoResponse));
        String userInfoUri = this.server.url("/user").toString();

        ClientRegistration clientRegistration = getClientRegistration(userInfoUri);

        // when
        OAuth2User user = this.userService.loadUser(new OAuth2UserRequest(clientRegistration, this.accessToken));
        log.info("OAuth2User : {}", user);

        // then
        assertThat(user).isNotNull();
        assertThat((String)user.getAttribute("user-name")).isEqualTo("user1");
        assertThat((String)user.getAttribute("first-name")).isEqualTo("first");
        assertThat((String)user.getAttribute("last-name")).isEqualTo("last");
        assertThat((String)user.getAttribute("middle-name")).isEqualTo("middle");
        assertThat((String)user.getAttribute("address")).isEqualTo("address");
        assertThat((String)user.getAttribute("email")).isEqualTo("user1@example.com");
        assertThat(user.getAuthorities().isEmpty()).isFalse();
        assertThat(user.getAuthorities().size()).isOne();

        List<Member> afterMembers = memberRepository.findAll();
        assertThat(afterMembers.size()).isOne();
    }

    @DisplayName("userInfoResponse 가 비어있는 경우 IllegalArgumentException 이 발생한다")
    @Transactional
    @Test
    public void loadUser_FailEmptyUserInfoResponse() {
        // given
        String emptyUserInfoResponse = "{}";

        this.server.enqueue(jsonResponse(emptyUserInfoResponse));
        String userInfoUri = this.server.url("/user").toString();

        ClientRegistration clientRegistration = getClientRegistration(userInfoUri);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> {
                    OAuth2User user = this.userService.loadUser(new OAuth2UserRequest(clientRegistration, this.accessToken));
                    log.info("OAuth2User : {}", user);
                }
        );

        List<Member> afterMembers = memberRepository.findAll();
        assertThat(afterMembers.size()).isZero();
    }

    @DisplayName("userInfoResponse 에 이메일이 없는 경우 OAuth2AuthenticationException 이 발생한다")
    @Transactional
    @Test
    public void loadUser_FailNotEnoughUserInfoResponse() {
        // given
        String noEmailUserInfoResponse =  "{\n"
                + "   \"name\": \"user1\",\n"
                + "   \""+userNameAttributeName+"\": \"user1\",\n"
                + "   \"first-name\": \"first\",\n"
                + "   \"last-name\": \"last\",\n"
                + "   \"middle-name\": \"middle\",\n"
                + "   \"address\": \"address\",\n"
                // + "   \"email\": \"user1@example.com\"\n"
                + "}\n";

        this.server.enqueue(jsonResponse(noEmailUserInfoResponse));
        String userInfoUri = this.server.url("/user").toString();

        ClientRegistration clientRegistration = getClientRegistration(userInfoUri);

        // when / then
        assertThrows(OAuth2AuthenticationException.class, () -> {
                    OAuth2User user = this.userService.loadUser(new OAuth2UserRequest(clientRegistration, this.accessToken));
                    log.info("OAuth2User : {}", user);
                }
        );

        List<Member> afterMembers = memberRepository.findAll();
        assertThat(afterMembers.size()).isZero();
    }

    @NotNull
    private ClientRegistration getClientRegistration(String userInfoUri) {
        return ClientRegistration.withRegistrationId("test")
                .clientId("testClientId")
                .clientSecret("testClientSecret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userInfoUri(userInfoUri)
                .userNameAttributeName(userNameAttributeName)
                .redirectUri(DUMMY_URI)
                .authorizationUri(DUMMY_URI)
                .tokenUri(DUMMY_URI)
                .jwkSetUri(DUMMY_URI)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .scope("read:user")
                .build();
    }

    private MockResponse jsonResponse(String json) {
        return new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).setBody(json);
    }
}