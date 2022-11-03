package ITerview.iterview.Dto.main;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionByCategoryDto {
    private List<QuestionDto> questions = new ArrayList<>();

}
