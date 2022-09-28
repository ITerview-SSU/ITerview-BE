package ITerview.iterview.repository;

import ITerview.iterview.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void 회원가입() throws Exception{

        User user = new User();
        user.setUsername("seungwoojoo");
        user.setPassword("1234");

        userRepository.save(user);

        org.assertj.core.api.Assertions.assertThat(user.getId()).isEqualTo(1);

    }
}