package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.MyPageMyQuestionsRequestDto;
import ITerview.iterview.Dto.main.MyPageMyQuestionsResponseDto;
import ITerview.iterview.Service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @PostMapping("myquestions")
    public MyPageMyQuestionsResponseDto getMyQuestions(@RequestBody MyPageMyQuestionsRequestDto myPageMyQuestionsRequestDto) {
        return myPageService.getMyQuestions(myPageMyQuestionsRequestDto);
    }
}
