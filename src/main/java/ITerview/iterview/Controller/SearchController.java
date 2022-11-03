package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.SearchResponseDto;
import ITerview.iterview.Service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/search")
    public SearchResponseDto getQuestions(@RequestParam String keyword){
        return searchService.getQuestions(keyword);
    }

}
