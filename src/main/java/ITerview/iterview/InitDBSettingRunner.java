package ITerview.iterview;

import ITerview.iterview.Service.CategoryCreationService;
import ITerview.iterview.Service.QuestionCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDBSettingRunner implements CommandLineRunner {
//    private final AuthService authService;
    private final CategoryCreationService categoryCreationService;
    private final QuestionCreationService questionCreationService;

    @Override
    public void run(String... args) throws Exception {
//        authService.createAuthorities();
//        categoryCreationService.createCategories();
        questionCreationService.createQuestions();
    }
}
