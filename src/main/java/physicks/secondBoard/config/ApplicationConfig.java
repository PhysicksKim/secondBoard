package physicks.secondBoard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableJpaAuditing
@Configuration
public class ApplicationConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

}
