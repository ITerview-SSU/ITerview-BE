package ITerview.iterview.Dto.transcription;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class TranscriptionItemAlternativesDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String confidence;
    private String content;

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }

        TranscriptionItemAlternativesDTO transcriptionItemAlternativesDTO = (TranscriptionItemAlternativesDTO) o;
        if (transcriptionItemAlternativesDTO.getContent() == null){
            return false;
        }
        return Objects.equals(getContent(), transcriptionItemAlternativesDTO.getContent());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(getContent());
    }

    @Override
    public String toString(){
        return "TranscriptionItemAlternativesDTO{" +
                "content=" + getContent() +
                ", confidence='" + getConfidence() + "'" +
                "}";
    }
}
