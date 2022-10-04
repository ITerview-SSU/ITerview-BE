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
    public TokenDTO reissue(@RequestBody TokenReqDTO tokenRequestDto) {
        return authService.reissue(tokenRequestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody TokenDTO tokenRequestDto){
        return authService.logout(tokenRequestDto.getAccessToken());
    }

    @PostMapping("/info")
    public MemberRespDTO info(@RequestBody TokenDTO tokenRequestDto){
        return authService.getInfo(tokenRequestDto.getAccessToken());
    }
}
