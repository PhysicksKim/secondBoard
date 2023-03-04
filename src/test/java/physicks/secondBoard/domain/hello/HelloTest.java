package physicks.secondBoard.domain.hello;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class HelloTest {

    @Autowired
    EntityManager em;

    @Autowired
    HelloRepository helloRepository;

    @Test
    public void hello_기본JPA_CRUD() {
        Hello hello = new Hello();
        hello.setName("hello1");

        helloRepository.save(hello);

        em.flush();
        em.clear();

        Optional<Hello> optFindHello = helloRepository.findById(hello.getId());
        if(optFindHello.get() == null) {
            fail("Optional<Hello>.get() == null. " +
                    "helloRepository에 정상적으로 save, findById가 되는지 확인 필요");
        }

        Hello findHello = optFindHello.get();

        assertThat(findHello.getId()).isEqualTo(hello.getId());
        assertThat(findHello.getName()).isEqualTo(hello.getName());
    }
}
