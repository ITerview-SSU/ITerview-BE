package ITerview.iterview.Dto.member;

import ITerview.iterview.Domain.Authority;
import ITerview.iterview.Domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberReqDTO {
    private String email;
    private String password;
    private String username;

    public Member toMember(PasswordEncoder passwordEncoder, Set<Authority> authorities) {
        return Member.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .activated(false)
                .authorities(authorities)
                .build();
    }

}