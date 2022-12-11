package ITerview.iterview.Service;

import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Dto.main.QuestionDto;
import ITerview.iterview.Dto.main.SearchResponseDto;
import ITerview.iterview.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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


            List<Category> categoryList = question.getCategories();

            List<String> categories = new ArrayList<>();
            categoryList.forEach(category -> categories.add(category.getCategory_name()));
            questionDto.setCategories(categories);

            List<Integer> categoryIds = new ArrayList<>();
            categoryList.forEach(category -> categoryIds.add(Math.toIntExact(category.getId())));
            questionDto.setCategoryIds(categoryIds);

            searchResponseDto.getQuestions().add(questionDto);
        }
        return searchResponseDto;
    }
}
