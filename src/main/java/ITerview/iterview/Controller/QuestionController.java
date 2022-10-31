package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.QuestionsDto;
import ITerview.iterview.Repository.QuestionRepository;
import ITerview.iterview.Service.QuestionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/api/questions")
    public QuestionsDto getQuestions(@RequestParam(name = "category") String category){
        return questionService.getQuestions(category);
    }

}
