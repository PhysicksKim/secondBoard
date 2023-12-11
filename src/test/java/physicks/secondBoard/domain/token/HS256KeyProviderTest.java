package physicks.secondBoard.domain.token;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

@Slf4j
@SpringBootTest
class HS256KeyProviderTest {

    @Autowired
    private HS256KeyProvider hs256KeyProvider;

    @DisplayName("SECRET_KEY 를 제공받습니다.")
    @Test
    void getSECRET_KEY() {
        // given / when
        SecretKey secretKey = hs256KeyProvider.getSECRET_KEY();

        // then
        Assertions.assertThat(secretKey).isNotNull();
    }

}