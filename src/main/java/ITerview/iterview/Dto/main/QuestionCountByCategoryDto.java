package ITerview.iterview.Dto.main;

import lombok.Builder;
import lombok.Data;

@Data
public class QuestionCountByCategoryDto {
    String categoryName;
    Integer questionCount;

    @Builder
    public QuestionCountByCategoryDto(String categoryName, Integer questionCount) {
        this.categoryName = categoryName;
        this.questionCount = questionCount;
    }
}
