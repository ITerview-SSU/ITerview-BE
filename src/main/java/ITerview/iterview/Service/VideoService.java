package ITerview.iterview.Service;

import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.main.Video;
import ITerview.iterview.Dto.main.VideoCreatedAtRequestDto;
import ITerview.iterview.Dto.main.VideoCreatedAtResponseDto;
import ITerview.iterview.Dto.main.VideoDeleteRequestDto;
import ITerview.iterview.ExceptionHandler.BizException;
import ITerview.iterview.ExceptionHandler.MemberExceptionType;
import ITerview.iterview.Jwt.TokenProvider;
import ITerview.iterview.Repository.MemberRepository;
import ITerview.iterview.Repository.VideoRepository;
import antlr.Token;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    public Member findByAccessToken(String accessToken){
        String userEmail = tokenProvider.getMemberEmailByToken(accessToken);
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BizException(MemberExceptionType.NOT_FOUND_USER));
        return member;
    }

    public void s3DeleteFile(String filename, String username) {
        s3Uploader.deleteFile(filename, username);
    }

    public VideoCreatedAtResponseDto getVideoCreatedAt(VideoCreatedAtRequestDto videoCreatedAtRequestDto) {
        String accessToken = videoCreatedAtRequestDto.getAccessToken();
        Long questionId = videoCreatedAtRequestDto.getQuestionId();

        Member member = findByAccessToken(accessToken);
        String createdAt;
        if (videoRepository.existsByMemberAndQuestion(questionId, member.getUsername())) {
             createdAt = videoRepository.getCreatedAt(questionId, member.getUsername());
        }else{
            createdAt = "";
        }

        VideoCreatedAtResponseDto videoCreatedAtResponseDto = new VideoCreatedAtResponseDto();
        videoCreatedAtResponseDto.setCreatedAt(createdAt);
        return videoCreatedAtResponseDto;
    }

    public ResponseEntity deleteVideo(VideoDeleteRequestDto videoDeleteRequestDto) {
        String accessToken = videoDeleteRequestDto.getAccessToken();
        Long questionId = videoDeleteRequestDto.getQuestionId();
        Member member = findByAccessToken(accessToken);
        String username = member.getUsername();

        String filename = videoRepository.getFilename(questionId, username);
        s3DeleteFile(filename, username);
        videoRepository.removeOne(questionId, username);

        return new ResponseEntity(HttpStatus.OK);
    }
}
