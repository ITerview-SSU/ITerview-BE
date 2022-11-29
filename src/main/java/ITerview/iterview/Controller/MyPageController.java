package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.MyPageMyQuestionsRequestDto;
import ITerview.iterview.Dto.main.MyPageMyQuestionsResponseDto;
import ITerview.iterview.Service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/myquestions/all")
    public MyPageMyQuestionsResponseDto getMyQuestions(@RequestHeader("Authorization") String accessToken) {
        return myPageService.getMyQuestions(new MyPageMyQuestionsRequestDto(accessToken.substring(7)));
    }
}
