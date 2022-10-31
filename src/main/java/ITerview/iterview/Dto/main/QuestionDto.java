package ITerview.iterview.Dto.main;

import lombok.Builder;
import lombok.Data;

@Data
public class QuestionDto {
    private Long questionId;
    private String questionString;

    @Builder
    public QuestionDto(Long questionId, String questionString) {
        this.questionId = questionId;
        this.questionString = questionString;
    }
}
