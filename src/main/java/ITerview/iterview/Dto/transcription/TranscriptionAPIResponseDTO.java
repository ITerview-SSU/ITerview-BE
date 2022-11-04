package ITerview.iterview.Dto.transcription;

import lombok.Builder;
import lombok.Data;

@Data
public class TranscriptionAPIResponseDTO {
    private String transcription;

    @Builder
    public TranscriptionAPIResponseDTO(String transcription) {
        this.transcription = transcription;
    }
}
