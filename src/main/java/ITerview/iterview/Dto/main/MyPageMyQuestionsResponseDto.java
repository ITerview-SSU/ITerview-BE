package ITerview.iterview.Dto.main;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyPageMyQuestionsResponseDto {
    private List<QuestionDto> questions = new ArrayList<>();
}
