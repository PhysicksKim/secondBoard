package physicks.secondBoard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import physicks.secondBoard.domain.member.login.H2UserDetailsService;
import physicks.secondBoard.domain.member.login.redirect.CustomAuthEntryPoint;
import physicks.secondBoard.domain.oauth.CustomOAuth2UserService;
import physicks.secondBoard.domain.user.Role;

/**
 * 설정에 사용된 보안 설정용 Bean 들은 SecurityBeans 에서 관리한다.
 * @see physicks.secondBoard.config.SecurityBeans
 */
@Configuration
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
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headers) -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthEntryPoint())
                )
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/", "/css/**","/images/**",
                                "/js/**","h2-console/**","/profile").permitAll() // index페이지 + 정적 파일 권한
                        .requestMatchers("/board/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .anonymous((anonymous) -> anonymous
                        .principal("anonymousUser")
                        .authorities(Role.GUEST.getKey())
                )
                .oauth2Login((oauth2Login) -> oauth2Login
                        .userInfoEndpoint((userInfoEndpoint) -> userInfoEndpoint
                                .userService(customOAuth2UserService)
                        )
                        .loginPage("/login").permitAll()
                )
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .failureHandler(authenticationFailureHandler)
                        .successHandler(authenticationSuccessHandler)
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                );

        return http.build();
    }

}