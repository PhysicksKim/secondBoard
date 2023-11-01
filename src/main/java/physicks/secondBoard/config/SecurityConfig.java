package physicks.secondBoard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import physicks.secondBoard.domain.oauth.CustomOAuth2UserService;

@RequiredArgsConstructor
@EnableWebSecurity(debug = false)
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
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
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                .formLogin()
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService)
                    .and()
                        .loginPage("/login").permitAll()
                .and()
                    .formLogin()
                        .loginPage("/login").permitAll();

        return http.build();
    }
}