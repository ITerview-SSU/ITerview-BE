package ITerview.iterview.Dto.transcription;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class TranscriptionResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String jobName;
    private String accountId;
    private TranscriptionResultDTO results;
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranscriptionResponseDTO transcriptionResponseDTO = (TranscriptionResponseDTO) o;
        if (transcriptionResponseDTO.getStatus() == null) {
            return false;
        }
        return Objects.equals(getStatus(), transcriptionResponseDTO.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getStatus());
    }

    @Override
    public String toString() {
        return "TranscriptionResponseDTO{" +
                "jobName=" + getJobName() +
                ", accountId='" + getAccountId() + "'" +
                ", status='" + getStatus() + "'" +
                ", results='" + getResults() + "'" +
                "}";
    }
}
