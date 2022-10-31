package ITerview.iterview.Service;

import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.IterviewApplication;
import ITerview.iterview.Repository.CategoryRepository;
import ITerview.iterview.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionCreationService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final String DELIMITER = "::";

    public void createQuestions(){
        List<List<String>> records = new ArrayList<>();
        String path = System.getProperty("user.dir") + "/src/main/resources/questions.txt";
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                List<String> questionParams = getRecordFromLine(scanner.nextLine());
                System.out.println(questionParams);
                records.add(questionParams);
            }
        } catch (FileNotFoundException e) {
            System.out.println("파일 없는데?");
        }
        System.out.println("records :" + records);

        for (List<String> questionParam : records){
            String question_name = questionParam.get(0);
            List<String> categoryNames = questionParam.subList(1, questionParam.size());
            List<Category> categories = new ArrayList<>();
            for (String categoryName : categoryNames){
                Category category = categoryRepository.findByCategory_name(categoryName);
                categories.add(category);
            }

            Question question = new Question();
            question.setQuestion(question_name);
            question.setCategories(categories);
            questionRepository.saveQuestion(question);
        }
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(line, DELIMITER);
        while(st.hasMoreElements()){
            values.add(st.nextToken());
        }
        return values;
    }
}
