package physicks.secondBoard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@Configuration
public class ApplicationConfig {

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        // Auditing 시 필요한 AuditorAware를 Bean으로 등록해준다.
        // 차후에 securityContext 에서 user 정보를 추출 하는 방식 등으로 수정 필요
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
