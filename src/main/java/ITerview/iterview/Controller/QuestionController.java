package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.QuestionByCategoryDto;
import ITerview.iterview.Dto.main.QuestionCountByCategoryDto;
import ITerview.iterview.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/questions")
    public QuestionByCategoryDto getQuestions(@RequestParam(name = "category") String categoryId){
        return questionService.getQuestions(categoryId);
    }

    @GetMapping("/questions/random")
    public QuestionByCategoryDto getRandomQuestions(@RequestParam(name = "category") String categoryId){
        return questionService.getRandomQuestions(categoryId);
    }

    @GetMapping("/questions/count")
    public QuestionCountByCategoryDto getQuestionCountByCategory(@RequestParam(name = "category") String categoryId){
        return questionService.getQuestionCountByCategory(categoryId);
    }

}
