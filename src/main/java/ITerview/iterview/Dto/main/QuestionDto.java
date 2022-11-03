package ITerview.iterview.Dto.main;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDto {
    private Long questionId;
    private String questionString;

    private List<String> categories = new ArrayList<>();

    @Builder
    public QuestionDto(Long questionId, String questionString) {
        this.questionId = questionId;
        this.questionString = questionString;
    }
}
