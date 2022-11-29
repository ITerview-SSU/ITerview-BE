package ITerview.iterview.Service;

import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Dto.main.QuestionCountByCategoryDto;
import ITerview.iterview.Dto.main.QuestionDto;
import ITerview.iterview.Dto.main.QuestionByCategoryDto;
import ITerview.iterview.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    public QuestionByCategoryDto getRandomQuestions(String category) {
        List<Question> questionList = questionRepository.findByCategory(category);
        Collections.shuffle(questionList);

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

    public QuestionCountByCategoryDto getQuestionCountByCategory(String category) {
        List<Question> questionList = questionRepository.findByCategory(category);
        Integer questionCount = questionList.size();

        QuestionCountByCategoryDto questionCountByCategoryDto = QuestionCountByCategoryDto.builder()
                .categoryName(category)
                .questionCount(questionCount)
                .build();

        return questionCountByCategoryDto;
    }
}
