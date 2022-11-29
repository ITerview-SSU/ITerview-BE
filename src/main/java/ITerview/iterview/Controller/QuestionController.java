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
    public QuestionByCategoryDto getQuestions(@RequestParam(name = "category") String category){
        return questionService.getQuestions(category);
    }

    @GetMapping("/questions/random")
    public QuestionByCategoryDto getRandomQuestions(@RequestParam(name = "category") String category){
        return questionService.getRandomQuestions(category);
    }

    @GetMapping("/questions/count")
    public QuestionCountByCategoryDto getQuestionCountByCategory(@RequestParam(name = "category") String category){
        return questionService.getQuestionCountByCategory(category);
    }

}
