package ITerview.iterview.Service;

import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Domain.main.Video;
import ITerview.iterview.Dto.main.MyPageMyQuestionsRequestDto;
import ITerview.iterview.Dto.main.MyPageMyQuestionsResponseDto;
import ITerview.iterview.Dto.main.QuestionDto;
import ITerview.iterview.ExceptionHandler.BizException;
import ITerview.iterview.ExceptionHandler.MemberExceptionType;
import ITerview.iterview.Jwt.TokenProvider;
import ITerview.iterview.Repository.MemberRepository;
import ITerview.iterview.Repository.QuestionRepository;
import ITerview.iterview.Repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    private final QuestionRepository questionRepository;
    private final VideoRepository videoRepository;

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public Member findByAccessToken(String accessToken) {
        String userEmail = tokenProvider.getMemberEmailByToken(accessToken);
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BizException(MemberExceptionType.NOT_FOUND_USER));
        return member;
    }

    public MyPageMyQuestionsResponseDto getMyQuestions(MyPageMyQuestionsRequestDto myPageMyQuestionsRequestDto) {
        String accessToken = myPageMyQuestionsRequestDto.getAccessToken();
        Member member = findByAccessToken(accessToken);
        List<Video> videos = videoRepository.findByMember(member);

        Set<Question> questionList = questionRepository.findByVideos(videos);

        MyPageMyQuestionsResponseDto myPageMyQuestionsResponseDto = new MyPageMyQuestionsResponseDto();
        for (Question question : questionList){
            QuestionDto questionDto = QuestionDto.builder()
                    .questionId(question.getId())
                    .questionString(question.getQuestion())
                    .build();

            List<Category> categoryList = question.getCategories();
            List<String> categories = new ArrayList<>();
            categoryList.forEach(category -> categories.add(category.getCategory_name()));
            questionDto.setCategories(categories);
            myPageMyQuestionsResponseDto.getQuestions().add(questionDto);
        }
        return myPageMyQuestionsResponseDto;
    }
}
