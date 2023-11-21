package physicks.secondBoard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import physicks.secondBoard.domain.member.login.H2UserDetailsService;
import physicks.secondBoard.domain.oauth.CustomOAuth2UserService;
import physicks.secondBoard.domain.user.Role;

/**
 * 설정에 사용된 보안 설정용 Bean 들은 SecurityBeans 에서 관리한다.
 * @see physicks.secondBoard.config.SecurityBeans
 */
@RequiredArgsConstructor
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final H2UserDetailsService h2UserDetailsService;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(h2UserDetailsService);

        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/css/**","/images/**",
                            "/js/**","h2-console/**","/profile").permitAll() // index페이지 + 정적 파일 권한
                    .antMatchers("/board/**").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
                .and()
                    .anonymous().authorities(Role.GUEST.getKey())
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService)
                        .and()
                            .loginPage("/login").permitAll()
                .and()
                    .formLogin()
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .failureHandler(authenticationFailureHandler)
                        .successHandler(authenticationSuccessHandler)
                .and()
                    .logout()
                    .logoutSuccessHandler(logoutSuccessHandler);

        return http.build();
    }

}