package physicks.secondBoard.domain.token;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

/**
 * HS256 Key 를 제공합니다.
 */
@Component
public class HS256KeyProvider {

    private final SecretKey SECRET_KEY; // 환경 변수로 주입 받음

    public HS256KeyProvider(SecretKey secretKey) {
        this.SECRET_KEY = secretKey;
    }

    public SecretKey getSECRET_KEY() {
        return SECRET_KEY;
    }
}
