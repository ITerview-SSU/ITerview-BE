package ITerview.iterview.Service;

import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryCreationService{
    private final CategoryRepository categoryRepository;

    public void createCategories(){
        List<String> categoryNames = new ArrayList<>(Arrays.asList("COMMON", "FE", "BE", "IOS", "ANDROID", "UIUX", "PM"));
        for(String categoryName : categoryNames){
            Category category = new Category();
            category.setCategory_name(categoryName);
            categoryRepository.saveCategory(category);
        }
    }

}
