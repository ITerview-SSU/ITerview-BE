package ITerview.iterview.Service;

import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Repository.CategoryRepository;
import ITerview.iterview.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
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
    private final String QUESTIONS_FILE = "questions.txt";

    public void createQuestions(){
        List<List<String>> records = new ArrayList<>();
        String path = System.getProperty("user.dir") + "/" + QUESTIONS_FILE;
        System.out.println("questions.txt 의 경로 : " +  path);
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                List<String> questionParams = getRecordFromLine(scanner.nextLine());
                records.add(questionParams);
            }
        }
        catch (Exception e) {
            System.out.println("Question file does not exist.");
        }

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
