package ITerview.iterview.Service;

import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Dto.main.QuestionByCategoryDto;
import ITerview.iterview.Dto.main.QuestionCountByCategoryDto;
import ITerview.iterview.Dto.main.QuestionDto;
import ITerview.iterview.Repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public QuestionByCategoryDto getQuestions(String categoryId){
        Category category = categoryRepository.findById(Long.parseLong(categoryId));

        List<Question> questionList = questionRepository.findByCategory(category.getCategory_name());

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

    public QuestionByCategoryDto getRandomQuestions(String categoryId) {
        Category category = categoryRepository.findById(Long.parseLong(categoryId));

        List<Question> questionList = questionRepository.findByCategory(category.getCategory_name());
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

    public QuestionCountByCategoryDto getQuestionCountByCategory(String categoryId) {
        Category category = categoryRepository.findById(Long.parseLong(categoryId));
        String categoryName = category.getCategory_name();

        List<Question> questionList = questionRepository.findByCategory(categoryName);
        Integer questionCount = questionList.size();

        QuestionCountByCategoryDto questionCountByCategoryDto = QuestionCountByCategoryDto.builder()
                .categoryName(categoryName)
                .questionCount(questionCount)
                .build();

        return questionCountByCategoryDto;
    }
}
