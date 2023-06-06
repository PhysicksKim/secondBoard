package physicks.secondBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import physicks.secondBoard.config.ApplicationConfig;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@Import({ApplicationConfig.class })
public class SecondBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecondBoardApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		// Auditing시 작성자가 누구인지 기록하는 AuditorAware 가 필요하므로 Bean으로 등록해준다.
		// 차후에 securityContext 에서 user 정보를 추출 하는 방식 등으로 수정 필요
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
