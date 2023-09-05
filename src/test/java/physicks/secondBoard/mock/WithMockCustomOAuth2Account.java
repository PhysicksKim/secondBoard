package physicks.secondBoard.mock;

import org.springframework.security.test.context.support.WithSecurityContext;
import physicks.secondBoard.domain.user.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomOAuth2AccountSecurityContextFactory.class)
public @interface WithMockCustomOAuth2Account {

    // String username() default "username";

    String name() default "name";

    String email() default "my@default.email";

    String picture() default "https://get_my_picture.com";

    Role role() default Role.MEMBER;

    String registrationId();

}
