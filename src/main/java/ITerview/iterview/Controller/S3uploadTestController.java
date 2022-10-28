package ITerview.iterview.Controller;

import ITerview.iterview.Service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class S3uploadTestController {
    private final S3Uploader s3Uploader;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("video") MultipartFile multipartFile) throws IOException{
        String [] values = s3Uploader.upload(multipartFile, "1avn");
        return values[0] + values[1];
    }
}
