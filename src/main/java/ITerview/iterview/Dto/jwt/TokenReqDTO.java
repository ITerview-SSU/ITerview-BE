package ITerview.iterview.Dto.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenReqDTO {
    private String accessToken;
    private String refreshToken;
}