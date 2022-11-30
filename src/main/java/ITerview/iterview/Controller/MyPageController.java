package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.MyPageMyQuestionsResponseDto;
import ITerview.iterview.Service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/answered/all")
    public MyPageMyQuestionsResponseDto getMyQuestions(@RequestHeader("Authorization") String bearerToken) {
        return myPageService.getMyQuestions(bearerToken);
    }

    @GetMapping("/answered/category")
    public void getMyQuestionsByCategory(@RequestHeader("Authorization") String accessToken, @RequestParam(name="category") String category){
        return;
    }
}
