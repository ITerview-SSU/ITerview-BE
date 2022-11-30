package ITerview.iterview.Service;

import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Domain.main.Video;
import ITerview.iterview.Dto.main.S3PreSignedUrlRequestDto;
import ITerview.iterview.ExceptionHandler.BizException;
import ITerview.iterview.ExceptionHandler.MemberExceptionType;
import ITerview.iterview.Jwt.TokenProvider;
import ITerview.iterview.Repository.MemberRepository;
import ITerview.iterview.Repository.QuestionRepository;
import ITerview.iterview.Repository.VideoRepository;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class S3PreSignedUrlService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final VideoRepository videoRepository;
    private final TokenProvider tokenProvider;
    private final AmazonS3Client amazonS3Client;
    private final TranscriptionService transcriptionService;

    public static final String BEARER_PREFIX = "Bearer ";

    private String resolveToken(String bearerToken) {
        // bearer : 123123123123123 -> return 123123123123123123
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }


    @Value("${cloud.aws.S3.bucket}")
    private String S3_BUCKET;

    public Member findByAccessToken(String accessToken){
        String userEmail = tokenProvider.getMemberEmailByToken(accessToken);
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BizException(MemberExceptionType.NOT_FOUND_USER));
        return member;
    }

    public void deleteFile_s3(String username, String filename){
        try {
            amazonS3Client.deleteObject(S3_BUCKET, username + "/" + filename);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }
    public void saveVideoEntity(Member member, Long questionId, String filename){
        // questionId를 통해서 Question 객체를 찾고, Question객체의 categories를 불러와서, video의 categories에 저장
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("해당 질문이 존재하지 않습니다."));

        // 해당 질문에 이미 비디오가 있을 경우 db와 s3에서 삭제
        if(videoRepository.existsByMemberAndQuestion(questionId, member.getUsername())){
            videoRepository.removeOne(questionId, member.getUsername());
            deleteFile_s3(member.getUsername(), filename);
        }

        // 카테고리 설정
        List<Category> categories = new ArrayList<>();
        for (Category category : question.getCategories()){
            categories.add(category);
        }

        Video video = new Video();
        video.setMember(member);
        video.setQuestion(question);
        video.setFilename(filename);

        video.updateCategories(categories);
        videoRepository.saveVideo(video);
    }


    public String getPreSignedURLForPut(S3PreSignedUrlRequestDto s3PreSignedUrlRequestDto, String bearerToken) {
        String accessToken = resolveToken(bearerToken);
        Member member = findByAccessToken(accessToken);
        String filename = s3PreSignedUrlRequestDto.getFilename();

        String preSignedURL = "";
        String newFileName = member.getUsername() + "/" + filename;

        saveVideoEntity(member, s3PreSignedUrlRequestDto.getQuestionId(), filename);

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60 * 24; // 24시
        expiration.setTime(expTimeMillis);


        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(S3_BUCKET, newFileName)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);
            URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            preSignedURL = url.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return preSignedURL;
    }

    public String getPreSignedURLForGet(S3PreSignedUrlRequestDto s3PreSignedUrlRequestDto, String bearerToken) {
        String accessToken = resolveToken(bearerToken);
        Member member = findByAccessToken(accessToken);
        String username = member.getUsername();
        Long questionId = s3PreSignedUrlRequestDto.getQuestionId();

        Video video;
        if(videoRepository.existsByMemberAndQuestion(questionId, username)) {
            video = videoRepository.findOne(questionId, username);
        }else{
            return "";
        }

        String filename = video.getFilename();

        String preSignedURL = "";
        String realFilename = username + "/" + filename;

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60 * 24; // 24시
        expiration.setTime(expTimeMillis);


        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(S3_BUCKET, realFilename)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            preSignedURL = url.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return preSignedURL;
    }
}
