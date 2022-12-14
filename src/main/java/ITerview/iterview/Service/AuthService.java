package ITerview.iterview.Service;

import ITerview.iterview.Domain.auth.Authority;
import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.auth.MemberAuth;
import ITerview.iterview.Domain.jwt.RefreshToken;
import ITerview.iterview.Dto.jwt.TokenDTO;
import ITerview.iterview.Dto.jwt.TokenReqDTO;
import ITerview.iterview.Dto.login.LoginReqDTO;
import ITerview.iterview.Dto.member.MemberReqDTO;
import ITerview.iterview.Dto.member.MemberRespDTO;
import ITerview.iterview.ExceptionHandler.AuthorityExceptionType;
import ITerview.iterview.ExceptionHandler.BizException;
import ITerview.iterview.ExceptionHandler.JwtExceptionType;
import ITerview.iterview.ExceptionHandler.MemberExceptionType;
import ITerview.iterview.Jwt.CustomEmailPasswordAuthToken;
import ITerview.iterview.Jwt.TokenProvider;
import ITerview.iterview.Repository.AuthorityRepository;
import ITerview.iterview.Repository.MemberRepository;
import ITerview.iterview.Repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public static final String BEARER_PREFIX = "Bearer ";

    private String resolveToken(String bearerToken) {
        // bearer : 123123123123123 -> return 123123123123123123
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Transactional
    public MemberRespDTO signup(MemberReqDTO memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new BizException(MemberExceptionType.DUPLICATE_USER);
        }

        // DB ?????? ROLE_USER??? ????????? ???????????? ????????????.
        Authority authority = authorityRepository
                .findByAuthorityName(MemberAuth.ROLE_USER).orElseThrow(()->new BizException(AuthorityExceptionType.NOT_FOUND_AUTHORITY));

        Set<Authority> set = new HashSet<>();
        set.add(authority);


        Member member = memberRequestDto.toMember(passwordEncoder, set);
        log.debug("member = {}",member);
        return MemberRespDTO.of(memberRepository.saveMember(member));
    }

    @Transactional
    public TokenDTO login(LoginReqDTO loginReqDTO) {
        CustomEmailPasswordAuthToken customEmailPasswordAuthToken = new CustomEmailPasswordAuthToken(loginReqDTO.getEmail(), loginReqDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(customEmailPasswordAuthToken);
        String email = authenticate.getName();
        Member member = customUserDetailsService.getMember(email);

        String accessToken = tokenProvider.createAccessToken(email, member.getAuthorities());
        // ?????? ?????? ????????? refreshToken??? ?????? ????????? ???????????? ?????????
        if (refreshTokenRepository.existsByKey(email)){
            refreshTokenRepository.deleteRefreshToken(refreshTokenRepository.findByKey(email).orElseThrow(()->new BizException(MemberExceptionType.NOT_FOUND_USER)));
        }
        String newRefreshToken = tokenProvider.createRefreshToken(email, member.getAuthorities());

        //refresh Token ??????
        refreshTokenRepository.saveRefreshToken(
                RefreshToken.builder()
                        .key(email)
                        .value(newRefreshToken)
                        .build()
        );

        return tokenProvider.createTokenDTO(accessToken, newRefreshToken);

    }

    @Transactional
    public TokenDTO reissue(TokenReqDTO tokenRequestDto) {
        /*
         *  accessToken ??? JWT Filter ?????? ???????????? ???
         * */
        String originAccessToken = tokenRequestDto.getAccessToken();
        String originRefreshToken = tokenRequestDto.getRefreshToken();

        // refreshToken ??????
        int refreshTokenFlag = tokenProvider.validateToken(originRefreshToken);

        log.debug("refreshTokenFlag = {}", refreshTokenFlag);

        //refreshToken ???????????? ????????? ?????? ????????? ????????????.
        if (refreshTokenFlag == -1) {
            throw new BizException(JwtExceptionType.BAD_TOKEN); // ????????? ???????????? ??????
        } else if (refreshTokenFlag == 2) {
            throw new BizException(JwtExceptionType.REFRESH_TOKEN_EXPIRED); // ???????????? ?????? ??????
        }

        // 2. Access Token ?????? Member Email ????????????
        Authentication authentication = tokenProvider.getAuthentication(originAccessToken);

        log.debug("Authentication = {}",authentication);

        // 3. ??????????????? Member Email ??? ???????????? Refresh Token ??? ?????????
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new BizException(MemberExceptionType.LOGOUT_MEMBER)); // ?????? ????????? ?????????


        // 4. Refresh Token ??????????????? ??????
        if (!refreshToken.getValue().equals(originRefreshToken)) {
            throw new BizException(JwtExceptionType.BAD_TOKEN); // ????????? ???????????? ????????????.
        }

        // 5. ????????? ?????? ??????
        String email = tokenProvider.getMemberEmailByToken(originAccessToken);
        Member member = customUserDetailsService.getMember(email);

        String newAccessToken = tokenProvider.createAccessToken(email, member.getAuthorities());
        String newRefreshToken = tokenProvider.createRefreshToken(email, member.getAuthorities());
        TokenDTO tokenDto = tokenProvider.createTokenDTO(newAccessToken, newRefreshToken);

        log.debug("refresh Origin = {}",originRefreshToken);
        log.debug("refresh New = {} ",newRefreshToken);
        // 6. ????????? ?????? ???????????? (dirtyChecking?????? ????????????)
        refreshToken.updateValue(newRefreshToken);

        // ?????? ??????
        return tokenDto;
    }

    public ResponseEntity logout(String bearerToken){
        String accessToken = resolveToken(bearerToken);
        String email = tokenProvider.getMemberEmailByToken(accessToken);
        RefreshToken refreshToken = refreshTokenRepository.findByKey(email)
                .orElseThrow(() -> new BizException(MemberExceptionType.LOGOUT_MEMBER));

        refreshTokenRepository.deleteRefreshToken(refreshToken);
        return new ResponseEntity(HttpStatus.OK);
    }

    public MemberRespDTO getInfo(String bearerToken){
        String accessToken = resolveToken(bearerToken);
        String email = tokenProvider.getMemberEmailByToken(accessToken);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BizException(MemberExceptionType.NOT_FOUND_USER));

        return MemberRespDTO.of(member);
    }
}
