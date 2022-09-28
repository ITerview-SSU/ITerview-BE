package ITerview.iterview.service;

import ITerview.iterview.domain.User;
import ITerview.iterview.dto.JwtRequestDto;
import ITerview.iterview.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception{
        //given
        User user = new User();
        user.setUsername("seungwoojoo");
        user.setPassword("1234");

        //when
        String username = authService.login(new JwtRequestDto(user.getUsername(), user.getPassword()));

        //then
        assertEquals(user, userRepository.findByUsername("seungwoojoo"));
    }
}