package physicks.secondBoard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import physicks.secondBoard.domain.member.login.CustomAuthenticationFailureHandler;
import physicks.secondBoard.domain.member.login.CustomAuthenticationSuccessHandler;
import physicks.secondBoard.domain.member.login.H2UserDetailsService;
import physicks.secondBoard.domain.oauth.CustomOAuth2UserService;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
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
                    .anyRequest().permitAll()
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
                .successHandler(customAuthenticationSuccessHandler)
                .and()
                    .logout()
                    .logoutSuccessUrl("/");

        return http.build();
    }

}