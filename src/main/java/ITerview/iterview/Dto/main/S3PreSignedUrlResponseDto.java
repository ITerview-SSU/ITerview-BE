package ITerview.iterview.Dto.main;

import lombok.Builder;
import lombok.Data;

@Data
public class S3PreSignedUrlResponseDto {
    private String preSignedUrl;

    @Builder
    public S3PreSignedUrlResponseDto(String preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }
}
