package ITerview.iterview.security;

import ITerview.iterview.domain.User;
import ITerview.iterview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        List<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) return null;

        else return new UserDetailsImpl(user.get(0));
    }
}
