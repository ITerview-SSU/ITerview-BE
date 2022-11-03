package ITerview.iterview.Service;

import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Dto.main.QuestionByCategoryDto;
import ITerview.iterview.Dto.main.QuestionDto;
import ITerview.iterview.Dto.main.SearchResponseDto;
import ITerview.iterview.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {
    private final QuestionRepository questionRepository;
    public SearchResponseDto getQuestions(String keyword) {
        List<Question> questionList = questionRepository.findByKeyword(keyword);

        SearchResponseDto searchResponseDto = new SearchResponseDto();
        for (Question question : questionList){
            QuestionDto questionDto = QuestionDto.builder()
                    .questionId(question.getId())
                    .questionString(question.getQuestion())
                    .build();
            searchResponseDto.getQuestions().add(questionDto);
        }
        return searchResponseDto;
    }
}
