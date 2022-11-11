package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.VideoCreatedAtRequestDto;
import ITerview.iterview.Dto.main.VideoCreatedAtResponseDto;
import ITerview.iterview.Dto.main.VideoDeleteRequestDto;
import ITerview.iterview.Service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/createdat")
    public VideoCreatedAtResponseDto getVideoCreatedAt(@RequestBody VideoCreatedAtRequestDto videoCreatedAtRequestDto, @RequestHeader("Authorization") String accessToken) {
        videoCreatedAtRequestDto.setAccessToken(accessToken.substring(7));
        return videoService.getVideoCreatedAt(videoCreatedAtRequestDto);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteVideo(@RequestBody VideoDeleteRequestDto videoDeleteRequestDto, @RequestHeader("Authorization") String accessToken) {
        videoDeleteRequestDto.setAccessToken(accessToken.substring(7));
        return videoService.deleteVideo(videoDeleteRequestDto);
    }
}
