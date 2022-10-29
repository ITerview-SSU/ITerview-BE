package ITerview.iterview.Dto.main;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VideoUploadDto {
    private Long questionId;
    private String accessToken;
    private MultipartFile video;

    @Builder
    public VideoUploadDto(Long questionId, String accessToken, MultipartFile video) {
        this.questionId = questionId;
        this.accessToken = accessToken;
        this.video = video;
    }
}
