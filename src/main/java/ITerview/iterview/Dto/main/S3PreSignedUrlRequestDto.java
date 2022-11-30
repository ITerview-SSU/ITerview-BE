package ITerview.iterview.Dto.main;

import lombok.Data;

@Data
public class S3PreSignedUrlRequestDto {
    private String filename;
    private Long questionId;
}
