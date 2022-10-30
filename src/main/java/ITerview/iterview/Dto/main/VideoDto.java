package ITerview.iterview.Dto.main;

import ITerview.iterview.Domain.main.Category;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VideoDto {
    private Long video_id;
    private String createdAt;
    private String caption;
    private String url;
    private Long memberId;
    private Long questionId;

    private List<VideoCategoryDto> categories = new ArrayList<>();
    @Builder
    public VideoDto(Long video_id, String createdAt, String caption, String url, Long memberId, Long questionId) {
        this.video_id = video_id;
        this.createdAt = createdAt;
        this.caption = caption;
        this.url = url;
        this.memberId = memberId;
        this.questionId = questionId;
    }
}
