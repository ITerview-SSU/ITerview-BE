package ITerview.iterview.Controller;

import ITerview.iterview.Dto.main.S3PreSignedUrlRequestDto;
import ITerview.iterview.Dto.main.S3PreSignedUrlResponseDto;
import ITerview.iterview.Dto.main.VideoDto;
import ITerview.iterview.Dto.main.VideoUploadDto;
import ITerview.iterview.Service.S3PreSignedUrlService;
import ITerview.iterview.Service.S3uploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class S3uploadController {

    private final S3uploadService s3uploadService;
    private final S3PreSignedUrlService s3PreSignedUrlService;


    @PostMapping("/upload")
    public VideoDto uploadFile(@ModelAttribute VideoUploadDto videoUploadDto) throws Exception{
        return s3uploadService.uploads(videoUploadDto);
    }

    @PostMapping("/uploadurl")
    public S3PreSignedUrlResponseDto getPreSignedUrlForPut(@RequestBody S3PreSignedUrlRequestDto s3PreSignedUrlRequestDto){
        String preSignedUrl = s3PreSignedUrlService.getPreSignedURLForPut(s3PreSignedUrlRequestDto);
        return S3PreSignedUrlResponseDto.builder().preSignedUrl(preSignedUrl).build();
    }

    @PostMapping("/viewurl")
    public S3PreSignedUrlResponseDto getPreSignedUrlForGet(@RequestBody S3PreSignedUrlRequestDto s3PreSignedUrlRequestDto){

        String preSignedUrl = s3PreSignedUrlService.getPreSignedURLForGet(s3PreSignedUrlRequestDto);
        return S3PreSignedUrlResponseDto.builder().preSignedUrl(preSignedUrl).build();
    }
}
