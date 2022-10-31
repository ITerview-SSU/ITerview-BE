package ITerview.iterview.Dto.main;

import ITerview.iterview.Domain.main.Question;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionsDto {
    private List<QuestionDto> questions = new ArrayList<>();

}
