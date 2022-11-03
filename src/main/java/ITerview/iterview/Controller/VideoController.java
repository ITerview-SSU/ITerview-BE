package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.VideoCreatedAtRequestDto;
import ITerview.iterview.Dto.main.VideoCreatedAtResponseDto;
import ITerview.iterview.Dto.main.VideoDeleteRequestDto;
import ITerview.iterview.Service.VideoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/createdat")
    public VideoCreatedAtResponseDto getVideoCreatedAt(@RequestBody VideoCreatedAtRequestDto videoCreatedAtRequestDto) {
        return videoService.getVideoCreatedAt(videoCreatedAtRequestDto);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteVideo(@RequestBody VideoDeleteRequestDto videoDeleteRequestDto) {
        return videoService.deleteVideo(videoDeleteRequestDto);
    }
}
