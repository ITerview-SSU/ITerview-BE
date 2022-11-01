package ITerview.iterview.Service;

import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.main.Video;
import ITerview.iterview.Dto.main.VideoCreatedAtRequestDto;
import ITerview.iterview.Dto.main.VideoCreatedAtResponseDto;
import ITerview.iterview.ExceptionHandler.BizException;
import ITerview.iterview.ExceptionHandler.MemberExceptionType;
import ITerview.iterview.Jwt.TokenProvider;
import ITerview.iterview.Repository.MemberRepository;
import ITerview.iterview.Repository.VideoRepository;
import antlr.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public Member findByAccessToken(String accessToken){
        String userEmail = tokenProvider.getMemberEmailByToken(accessToken);
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BizException(MemberExceptionType.NOT_FOUND_USER));
        return member;
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
}
