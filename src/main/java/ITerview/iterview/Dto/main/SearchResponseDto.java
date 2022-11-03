package ITerview.iterview.Dto.main;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResponseDto {
    private List<QuestionDto> questions = new ArrayList<>();
}
