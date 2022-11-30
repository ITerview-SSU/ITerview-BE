package ITerview.iterview.Controller;


import ITerview.iterview.Dto.jwt.TokenDTO;
import ITerview.iterview.Dto.jwt.TokenReqDTO;
import ITerview.iterview.Dto.login.LoginReqDTO;
import ITerview.iterview.Dto.member.MemberReqDTO;
import ITerview.iterview.Service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ITerview.iterview.Dto.member.MemberRespDTO;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public MemberRespDTO signup(@RequestBody MemberReqDTO memberRequestDto) {
        log.debug("memberRequestDto = {}",memberRequestDto);
        return authService.signup(memberRequestDto);
    }
    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginReqDTO loginReqDTO) {
        return authService.login(loginReqDTO);
    }

    @PostMapping("/reissue")
    public TokenDTO reissue(@RequestHeader("Authorization") String accessToken, @RequestHeader("refresh-token") String refreshToken) {
        return authService.reissue(new TokenReqDTO(accessToken.substring(7), refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String bearerToken){
        return authService.logout(bearerToken);
    }

    @PostMapping("/info")
    public MemberRespDTO info(@RequestHeader("Authorization") String bearerToken){
        return authService.getInfo(bearerToken);
    }
}
