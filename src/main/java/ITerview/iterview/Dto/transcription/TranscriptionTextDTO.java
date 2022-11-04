package ITerview.iterview.Dto.transcription;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class TranscriptionTextDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String transcript;

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }

        TranscriptionTextDTO transcriptionTextDTO = (TranscriptionTextDTO) o;
        if(transcriptionTextDTO.getTranscript() == null){
            return false;
        }
        return Objects.equals(getTranscript(), transcriptionTextDTO.getTranscript());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTranscript());
    }

    @Override
    public String toString() {
        return "TranscriptionTextDTO{" +
                "transcript=" + getTranscript() +
                "}";
    }
}
