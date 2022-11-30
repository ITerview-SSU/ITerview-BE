package ITerview.iterview.Service;

import ITerview.iterview.Config.S3Uploader;
import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Dto.main.VideoCreatedAtRequestDto;
import ITerview.iterview.Dto.main.VideoCreatedAtResponseDto;
import ITerview.iterview.Dto.main.VideoDeleteRequestDto;
import ITerview.iterview.ExceptionHandler.BizException;
import ITerview.iterview.ExceptionHandler.MemberExceptionType;
import ITerview.iterview.Jwt.TokenProvider;
import ITerview.iterview.Repository.MemberRepository;
import ITerview.iterview.Repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    public static final String BEARER_PREFIX = "Bearer ";

    private String resolveToken(String bearerToken) {
        // bearer : 123123123123123 -> return 123123123123123123
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Member findByAccessToken(String accessToken){
        String userEmail = tokenProvider.getMemberEmailByToken(accessToken);
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BizException(MemberExceptionType.NOT_FOUND_USER));
        return member;
    }

    public void s3DeleteFile(String filename, String username) {
        s3Uploader.deleteFile(filename, username);
    }

    public VideoCreatedAtResponseDto getVideoCreatedAt(VideoCreatedAtRequestDto videoCreatedAtRequestDto, String bearerToken) {
        String accessToken = resolveToken(bearerToken);
        Long questionId = videoCreatedAtRequestDto.getQuestionId();

        Member member = findByAccessToken(accessToken);
        String createdAt;
        if (videoRepository.existsByMemberAndQuestion(questionId, member.getUsername())) {
             createdAt = videoRepository.getCreatedAt(questionId, member.getUsername());
             createdAt = refineCreatedAt(createdAt);
        }else{
            createdAt = "";
        }

        VideoCreatedAtResponseDto videoCreatedAtResponseDto = new VideoCreatedAtResponseDto();
        videoCreatedAtResponseDto.setCreatedAt(createdAt);
        return videoCreatedAtResponseDto;
    }

    public String refineCreatedAt(String createdAt){
        //"2022-11-28T01:28:50.331511"
        String[] splitCreatedAt =  createdAt.split("T");
        String date = splitCreatedAt[0];
        date = date.replace("-", ". ");

        String time = splitCreatedAt[1];
        String hour = time.substring(0, 2);
        String minute = time.substring(3, 5);
        Integer hourInt = Integer.parseInt(hour);
        String ampm;
        if(hourInt > 12){
            ampm = "PM";
            hourInt -= 12;
        }else{
            ampm = "AM";
        }
        hour = (hourInt >= 10) ? "" + hour : "0" + hourInt;

        return date + ". " + ampm + " " + hour + ":" + minute;
    }

    public ResponseEntity deleteVideo(VideoDeleteRequestDto videoDeleteRequestDto, String bearerToken) {
        String accessToken = resolveToken(bearerToken);
        Long questionId = videoDeleteRequestDto.getQuestionId();
        Member member = findByAccessToken(accessToken);
        String username = member.getUsername();

        String filename = videoRepository.getFilename(questionId, username);
        s3DeleteFile(filename, username);
        videoRepository.removeOne(questionId, username);

        return new ResponseEntity(HttpStatus.OK);
    }
}
