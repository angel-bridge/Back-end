package angel_bridge.angel_bridge_server.global.s3.service;

import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final AmazonS3 amazonS3;

    /**
     * 이미지 이름 변경 메서드
     * 변경된 이미지 이름 : randomUUID + 파일 확장자
     */
    private String changedImageName(String originName) {
        String random = UUID.randomUUID().toString();
        int dotIndex = originName.lastIndexOf(".");
        String extension = dotIndex == -1 ? "" : originName.substring(dotIndex);
        return random + extension;
    }

    /**
     * presigned url 발급
     * @return 10분 유효한 이미지 조회용 url
     */
    public String getImageUrl(String fileName) {

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(new Date(System.currentTimeMillis() + 600000));

        return amazonS3.generatePresignedUrl(request).toString();
    }

    /**
     * 이미지 업로드 메서드
     * @return 저장된 이미지의 이름 반환
     */
    @Transactional
    public String uploadImage(MultipartFile imageFile) throws IOException {

        if (imageFile == null || imageFile.isEmpty()) {
            throw new ApplicationException(EMPTY_FILE_EXCEPTION);
        }

        // 1. 고유한 파일 이름 생성
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new ApplicationException(INVALID_FILENAME_EXCEPTION);
        }
        String changedName = changedImageName(originalFilename);

        // 2. S3에 업로드할 파일의 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageFile.getSize());
        metadata.setContentType(imageFile.getContentType());

        // 3. S3에 파일 업로드
        amazonS3.putObject(bucketName, changedName, imageFile.getInputStream(), metadata);

        // 4. 저장된 파일 이름 반환
        return changedName;
    }

    /**
     * S3 bucket에서 이미지 삭제
     */
    @Transactional
    public void deleteImage(String fileName) {

        // 1. file exist 검사
        if (!amazonS3.doesObjectExist(bucketName, fileName)) {
            throw new ApplicationException(NOT_FOUND_FILE_EXCEPTION);
        }

        // 2. S3 bucket에서 파일 삭제
        try {
            amazonS3.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            throw new ApplicationException(FILE_DELETE_FAILED_EXCEPTION);
        }
    }
}
