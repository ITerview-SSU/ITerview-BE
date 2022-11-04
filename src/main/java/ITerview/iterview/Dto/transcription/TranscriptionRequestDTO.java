package ITerview.iterview.Dto.transcription;

import lombok.Builder;
import lombok.Data;

@Data
public class TranscriptionRequestDTO {
    private String accessToken;
    private Long questionId;

    @Builder
    public TranscriptionRequestDTO(String accessToken, Long questionId) {
        this.accessToken = accessToken;
        this.questionId = questionId;
    }
}
