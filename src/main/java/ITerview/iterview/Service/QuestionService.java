package ITerview.iterview.Service;

import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Dto.main.QuestionDto;
import ITerview.iterview.Dto.main.QuestionByCategoryDto;
import ITerview.iterview.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionByCategoryDto getQuestions(String category){
        List<Question> questionList = questionRepository.findByCategory(category);

        QuestionByCategoryDto questionByCategoryDto = new QuestionByCategoryDto();
        for (Question question : questionList){
            QuestionDto questionDto = QuestionDto.builder()
                    .questionId(question.getId())
                    .questionString(question.getQuestion())
                    .build();
            questionByCategoryDto.getQuestions().add(questionDto);
        }
        return questionByCategoryDto;
    }
}
