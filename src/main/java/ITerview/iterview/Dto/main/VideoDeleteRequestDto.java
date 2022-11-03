package ITerview.iterview.Dto.main;

import lombok.Data;

@Data
public class VideoDeleteRequestDto {
    private String accessToken;
    private Long questionId;
}
