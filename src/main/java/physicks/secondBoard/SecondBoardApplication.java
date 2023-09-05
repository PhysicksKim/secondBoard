package physicks.secondBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
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
		// Auditing 시 필요한 AuditorAware를 Bean으로 등록해준다.
		// 차후에 securityContext 에서 user 정보를 추출 하는 방식 등으로 수정 필요
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
