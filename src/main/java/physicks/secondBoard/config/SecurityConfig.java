package physicks.secondBoard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import physicks.secondBoard.domain.member.login.CustomAuthenticationFailureHandler;
import physicks.secondBoard.domain.member.login.H2UserDetailsService;
import physicks.secondBoard.domain.oauth.CustomOAuth2UserService;
import physicks.secondBoard.domain.user.Role;

@RequiredArgsConstructor
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final H2UserDetailsService h2UserDetailsService;

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
                        .failureHandler(customAuthenticationFailureHandler)
                        .successHandler(authenticationSuccessHandler)
                .and()
                    .logout()
                        .logoutSuccessUrl("/");

        return http.build();
    }

}