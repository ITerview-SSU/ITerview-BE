package ITerview.iterview.Dto.main;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VideoDto {
    private Long video_id;
    private String createdAt;
    private String transcription;
    private String url;
    private Long memberId;
    private Long questionId;

    private List<VideoCategoryDto> categories = new ArrayList<>();
    @Builder
    public VideoDto(Long video_id, String createdAt, String transcription, String url, Long memberId, Long questionId) {
        this.video_id = video_id;
        this.createdAt = createdAt;
        this.transcription = transcription;
        this.url = url;
        this.memberId = memberId;
        this.questionId = questionId;
    }
}
