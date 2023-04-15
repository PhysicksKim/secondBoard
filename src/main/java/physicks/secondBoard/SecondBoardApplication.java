package physicks.secondBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
public class SecondBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecondBoardApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		// 차후에 경우에 따라서 securityContext 에서 user 정보를 추출 하는 등으로 수정
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
