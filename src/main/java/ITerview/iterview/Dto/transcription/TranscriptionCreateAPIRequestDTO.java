package ITerview.iterview.Dto.transcription;

import lombok.Data;

@Data
public class TranscriptionCreateAPIRequestDTO {
    private String accessToken;
    private Long questionId;
}
