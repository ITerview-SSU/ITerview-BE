package ITerview.iterview.Dto.transcription;

import lombok.Data;

@Data
public class TranscriptionAPIRequestDTO {
    private String accessToken;
    private Long questionId;

}