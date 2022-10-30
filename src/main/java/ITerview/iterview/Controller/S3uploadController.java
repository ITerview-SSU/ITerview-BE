package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.VideoDto;
import ITerview.iterview.Dto.main.VideoUploadDto;
import ITerview.iterview.Service.S3Uploader;
import ITerview.iterview.Service.S3uploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class S3uploadController {

    private final S3uploadService s3uploadService;


    @PostMapping("/upload")
    public VideoDto uploadFile(@ModelAttribute VideoUploadDto videoUploadDto) throws Exception{
        return s3uploadService.uploads(videoUploadDto);
    }
}
