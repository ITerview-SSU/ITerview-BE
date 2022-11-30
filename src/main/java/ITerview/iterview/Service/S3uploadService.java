package ITerview.iterview.Service;

import ITerview.iterview.Config.S3Uploader;
import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Domain.main.Video;
import ITerview.iterview.Dto.main.VideoCategoryDto;
import ITerview.iterview.Dto.main.VideoDto;
import ITerview.iterview.Dto.main.VideoUploadDto;
import ITerview.iterview.ExceptionHandler.BizException;
import ITerview.iterview.ExceptionHandler.MemberExceptionType;
import ITerview.iterview.Jwt.TokenProvider;
import ITerview.iterview.Repository.MemberRepository;
import ITerview.iterview.Repository.QuestionRepository;
import ITerview.iterview.Repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class S3uploadService {
    private final S3Uploader s3Uploader;
    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final TokenProvider tokenProvider;
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

    public VideoDto uploads(VideoUploadDto videoUploadDto, String bearerToken) throws Exception{
        String accessToken = resolveToken(bearerToken);
        Long questionId = videoUploadDto.getQuestionId();
        MultipartFile videoFile = videoUploadDto.getVideo();


        // 현재 로그인한 사용자 찾기
        Member member = findByAccessToken(accessToken);
        String username = member.getUsername();

        // questionId를 통해서 Question 객체를 찾고, Question객체의 categories를 불러와서, video의 categories에 저장
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("해당 질문이 존재하지 않습니다."));

        // 카테고리 설정
        List<Category> categories = new ArrayList<>();
        for (Category category : question.getCategories()){
            categories.add(category);
        }

        // 비디오 s3에 업로드
        String videoURL = s3Uploader.upload(videoFile, username)[1];

        Video video = new Video(member, question, videoURL);
        video.updateCategories(categories);
        videoRepository.saveVideo(video);

        VideoDto videoDto =  VideoDto.builder()
                .video_id(video.getId())
                .createdAt(String.valueOf(video.getCreatedAt()))
                .transcription(video.getTranscription())
                .url(video.getUrl())
                .memberId(video.getMember().getId())
                .questionId(video.getQuestion().getId())
                .build();

        for(Category category : categories){
            VideoCategoryDto videoCategoryDto = VideoCategoryDto.builder()
                    .category_id(category.getId())
                    .build();
            videoDto.getCategories().add(videoCategoryDto);
        }

        return videoDto;
    }

}
