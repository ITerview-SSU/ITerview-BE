package ITerview.iterview.Service;

import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Dto.main.QuestionDto;
import ITerview.iterview.Dto.main.QuestionsDto;
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

    public QuestionsDto getQuestions(String category){
        List<Question> questionList = questionRepository.findByCategory(category);

        QuestionsDto questionsDto = new QuestionsDto();
        for (Question question : questionList){
            QuestionDto questionDto = QuestionDto.builder()
                    .questionId(question.getId())
                    .questionString(question.getQuestion())
                    .build();
            questionsDto.getQuestions().add(questionDto);
        }
        return questionsDto;
    }
}
