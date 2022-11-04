package ITerview.iterview.Service;

import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.main.Video;
import ITerview.iterview.Dto.transcription.*;
import ITerview.iterview.ExceptionHandler.BizException;
import ITerview.iterview.ExceptionHandler.MemberExceptionType;
import ITerview.iterview.Jwt.TokenProvider;
import ITerview.iterview.Repository.MemberRepository;
import ITerview.iterview.Repository.VideoRepository;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.amazonaws.services.transcribe.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class TranscriptionService {
    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.S3.bucket}")
    private String bucket;

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;

    public Member findByAccessToken(String accessToken){
        String userEmail = tokenProvider.getMemberEmailByToken(accessToken);
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BizException(MemberExceptionType.NOT_FOUND_USER));
        return member;
    }

    public String findFilenameByQuestionIdAndUsername(Long questionId, String username){
        return videoRepository.getFilename(questionId, username);
    }

    public AmazonTranscribe transcribeClient() {
        System.out.println("Initialize Transcribe Client");
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        return AmazonTranscribeClientBuilder.standard().withCredentials(awsStaticCredentialsProvider)
                .withRegion(region).build();
    }

    /* **** Start Transcription Job Method******/
    public StartTranscriptionJobResult startTranscriptionJob(String key){
        Media media = new Media().withMediaFileUri(s3Client.getUrl(bucket, key).toExternalForm());
        String jobName = key.replace("/", "__");
        StartTranscriptionJobRequest startTranscriptionJobRequest = new StartTranscriptionJobRequest()
                .withLanguageCode(LanguageCode.KoKR).withTranscriptionJobName(jobName).withMedia(media);
        StartTranscriptionJobResult startTranscriptionJobResult = transcribeClient()
                .startTranscriptionJob(startTranscriptionJobRequest);
        return startTranscriptionJobResult;
    }

    /* *** Get Transcription Job Result method *********/
    public GetTranscriptionJobResult getTranscriptionJobResult(String jobName){
        System.out.println("Get Transcription Job Result By Job Name : " + jobName);
        GetTranscriptionJobRequest getTranscriptionJobRequest = new GetTranscriptionJobRequest()
                .withTranscriptionJobName(jobName);
        Boolean resultFound = false;
        TranscriptionJob transcriptionJob = new TranscriptionJob();
        GetTranscriptionJobResult getTranscriptionJobResult = new GetTranscriptionJobResult();

        while(resultFound == false){
            getTranscriptionJobResult = transcribeClient().getTranscriptionJob(getTranscriptionJobRequest);
            transcriptionJob = getTranscriptionJobResult.getTranscriptionJob();
            if(transcriptionJob.getTranscriptionJobStatus()
                    .equalsIgnoreCase(TranscriptionJobStatus.COMPLETED.name())){
                return getTranscriptionJobResult;
            }else if(transcriptionJob.getTranscriptionJobStatus()
                    .equalsIgnoreCase(TranscriptionJobStatus.FAILED.name())){
                return null;
            }else if(transcriptionJob.getTranscriptionJobStatus()
                    .equalsIgnoreCase(TranscriptionJobStatus.IN_PROGRESS.name())){
                try{
                    Thread.sleep(10000);
                }catch (InterruptedException e){
                    System.out.println("Interrupted Exception : " + e.getMessage());
                }
            }
        }
        return getTranscriptionJobResult;
    }

    /*  Download Transcription Result from URI Method *********/
    public TranscriptionResponseDTO downloadTranscriptionResponse(String uri){
        System.out.println("Download Transcription Result from Transcribe URL : " + uri);
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder().url(uri).build();
        Response response;
        try{
            response = okHttpClient.newCall(request).execute();
            String body = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            response.close();
            return objectMapper.readValue(body, TranscriptionResponseDTO.class);

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /* **** Delete Transcription Job Method ************
     * TO delete transcription job after getting result****/
    public void deleteTranscriptionJob(String jobName){
        System.out.println("Delete Transcription Job from amazon Transcribe : " + jobName);
        DeleteTranscriptionJobRequest deleteTranscriptionJobRequest = new DeleteTranscriptionJobRequest()
                .withTranscriptionJobName(jobName);
        transcribeClient().deleteTranscriptionJob(deleteTranscriptionJobRequest);
    }

    /*  Extract Speech Text method that combines all methods to a single method******
     ***** You can do skip upload delete methods if you want to just process file in AWS
     ***** by passing key for filename in bucket and create media *****/

    public TranscriptionResponseDTO extractSpeechTextFromVideo(TranscriptionRequestDTO transcriptionRequestDTO) {
        String username = findByAccessToken(transcriptionRequestDTO.getAccessToken()).getUsername();
        String filename = findFilenameByQuestionIdAndUsername(transcriptionRequestDTO.getQuestionId(), username);
        System.out.println("Request to extract Speech Text from Video : " + username + "/" + filename);

        // Start Transcription Job and get result
        StartTranscriptionJobResult startTranscriptionJobResult = startTranscriptionJob(username + "/" + filename);

        // Get name of job started for the file
        String transcriptionJobName = startTranscriptionJobResult.getTranscriptionJob().getTranscriptionJobName();

        // Get result after the procesiing is complete
        GetTranscriptionJobResult getTranscriptionJobResult = getTranscriptionJobResult(transcriptionJobName);

        // Url of result file for transcription
        String transcriptFileUriString = getTranscriptionJobResult.getTranscriptionJob().getTranscript().getTranscriptFileUri();

        // Get the transcription response by downloading the file
        TranscriptionResponseDTO transcriptionResponseDTO = downloadTranscriptionResponse(transcriptFileUriString);

        //Delete the transcription job after finishing or it will get deleted after 90 days automatically if you do not call
        deleteTranscriptionJob(transcriptionJobName);

        return transcriptionResponseDTO;
    }


    public TranscriptionAPIResponseDTO getVideoTranscription(TranscriptionAPIRequestDTO transcriptionAPIRequestDTO) {
        String accessToken = transcriptionAPIRequestDTO.getAccessToken();
        Long questionId = transcriptionAPIRequestDTO.getQuestionId();

        Member member = findByAccessToken(accessToken);
        String username = member.getUsername();

        Video video = videoRepository.findOne(questionId, username);
        String transcription = video.getTranscription();

        return TranscriptionAPIResponseDTO.builder().transcription(transcription).build();

    }

    public String getTranscription(TranscriptionRequestDTO transcriptionRequestDTO){
        String transcription = "";
        TranscriptionResponseDTO response = extractSpeechTextFromVideo(transcriptionRequestDTO);
        List<TranscriptionTextDTO> transcripts = response.getResults().getTranscripts();
        for (TranscriptionTextDTO transcript : transcripts){
            transcription = transcription + transcript.getTranscript();
        }
        return transcription;
    }

    public ResponseEntity createTranscription(TranscriptionCreateAPIRequestDTO transcriptionCreateAPIRequestDTO) {
        String accessToken = transcriptionCreateAPIRequestDTO.getAccessToken();
        Long questionId = transcriptionCreateAPIRequestDTO.getQuestionId();

        Member member = findByAccessToken(accessToken);
        String username = member.getUsername();

        try {
            Video video = videoRepository.findOne(questionId, username);
            // 비디오에 transcription이 설정되지 않았을 경우 transcription 생성
            if (video.getTranscription() == null) {
                String transcription = getTranscription(new TranscriptionRequestDTO(accessToken, questionId));
                video.setTranscription(transcription);
            }
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
