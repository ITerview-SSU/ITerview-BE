package ITerview.iterview.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class JwtRequestDto {
    private String username;
    private String password;
    
    public JwtRequestDto(String username, String password){
        this.username = username;
        this.password= password;
    }
}
