package mn.delivery.system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.s3.ImageType;
import mn.delivery.system.dto.MoveFileRequest;
import mn.delivery.system.exception.AwsException;
import mn.delivery.system.exception.ContentTypeException;
import mn.delivery.system.properties.AwsRekognitionProperties;
import mn.delivery.system.util.ImageTypeResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AwsRekognitionProperties rekognitionProperties;

    private S3Client s3Client;

    private S3Utilities s3Utilities;

    @PostConstruct
    private void init() {
        log.info("Initializing s3 bucket service with: " + rekognitionProperties.getAccessKey());
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                rekognitionProperties.getAccessKey(),
                rekognitionProperties.getSecretKey());
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCreds);

        s3Client = S3Client.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(credentialsProvider)
                .build();

        s3Utilities = S3Utilities.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }

    public String uploadStream(
            String fileKey,
            String name,
            String contentType,
            InputStream inputStream,
            String entity,
            Map<String, String> metaData
    ) throws AwsServiceException, IOException, ContentTypeException {
        log.info("{} -> uploading file {} to bucket: {}", fileKey, name, rekognitionProperties.getBucket());

        ImageType imageType = ImageTypeResolver.resolve(entity);
        ImageService.checkContentType(imageType, contentType);

        try {
            byte[] bytes = ImageService.resize(inputStream.readAllBytes(), imageType, contentType);

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(rekognitionProperties.getBucket())
                    .key(fileKey)
                    .contentType(contentType)
                    .metadata(metaData)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            // TODO append file extension
            PutObjectResponse putResponse = s3Client.putObject(
                    objectRequest,
                    RequestBody.fromBytes(bytes)
            );
            log.info(fileKey + " -> file uploaded with etag: {}", putResponse.eTag());

            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(rekognitionProperties.getBucket())
                    .key(fileKey)
                    .build();
            return replaceUrl(s3Utilities.getUrl(getUrlRequest).toString());
        } catch (Exception ex) {
            log.error("upload error: {}", ex.getMessage(), ex);
            throw new IOException(ex.getMessage());
        }
    }

    public String uploadStream(
            String fileKey,
            String name,
            String contentType,
            byte[] bytes,
            String entity,
            Map<String, String> metaData
    ) throws AwsServiceException, IOException, ContentTypeException {
        log.info("{} -> uploading file {} to bucket: {}", fileKey, name, rekognitionProperties.getBucket());

        ImageType imageType = ImageTypeResolver.resolve(entity);
        ImageService.checkContentType(imageType, contentType);

        try {
            bytes = ImageService.resize(bytes, imageType, contentType);

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(rekognitionProperties.getBucket())
                    .key(fileKey)
                    .contentType(contentType)
                    .metadata(metaData)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            // TODO append file extension
            PutObjectResponse putResponse = s3Client.putObject(
                    objectRequest,
                    RequestBody.fromBytes(bytes)
            );
            log.info(fileKey + " -> file uploaded with etag: {}", putResponse.eTag());

            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(rekognitionProperties.getBucket())
                    .key(fileKey)
                    .build();
            return replaceUrl(s3Utilities.getUrl(getUrlRequest).toString());
        } catch (Exception ex) {
            log.error("upload error: {}", ex.getMessage(), ex);
            throw new IOException(ex.getMessage());
        }
    }

    public String moveTempFile(MoveFileRequest request) throws AwsServiceException, AwsException {
        log.info("move request: " + request);

        String entity = request.getEntity();
        String sourceFileKey = resolveFileKey(request.getFileUrl());

        String newFileName = request.getFileName(sourceFileKey.replace(S3BucketFolder.TEMP, ""));

        String targetFileKey = (ObjectUtils.isEmpty(entity) ? "" : entity) + newFileName;

        log.info("sourceFileKey: " + sourceFileKey);
        log.info("fileName: " + newFileName);
        log.info("bucket: " + rekognitionProperties.getBucket());

        if (!ifExists(sourceFileKey))
            throw new AwsException("Файл олдсонгүй");

        s3Client.copyObject(CopyObjectRequest
                .builder()
                .sourceBucket(rekognitionProperties.getBucket())
                .sourceKey(sourceFileKey)
                .destinationBucket(rekognitionProperties.getBucket())
                .destinationKey(targetFileKey)
                .metadata(request.getMetaData(targetFileKey, newFileName))
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build());

        if (!request.isCopy())
            deleteFile(sourceFileKey);

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(rekognitionProperties.getBucket())
                .key(targetFileKey)
                .build();
        return replaceUrl(s3Utilities.getUrl(getUrlRequest).toString());
    }

    public GetObjectAttributesResponse getAttributes(String fileKey) {
        GetObjectAttributesRequest objectAttributesRequest = GetObjectAttributesRequest.builder()
                .bucket(rekognitionProperties.getBucket())
                .key(fileKey)
                .objectAttributes(
                        ObjectAttributes.E_TAG,
                        ObjectAttributes.OBJECT_SIZE,
                        ObjectAttributes.CHECKSUM
                )
                .build();
        return s3Client.getObjectAttributes(objectAttributesRequest);
//        log.info("eTag -> " + objectAttributesResponse.eTag());
//        log.info("objectSize -> " + objectAttributesResponse.objectSize());
//        log.info("checksum -> " + objectAttributesResponse.checksum());
    }

    public boolean ifExists(String fileKey) {
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(rekognitionProperties.getBucket())
                .key(fileKey)
                .build();

        try (ResponseInputStream<?> stream = s3Client.getObject(getObjectRequest)) {
            return true;
        } catch (IOException | S3Exception e) {
            return false;
        }
    }

    public void deleteFile(String fileKey) throws AwsServiceException {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(rekognitionProperties.getBucket())
                .key(fileKey)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    public void deleteFileByUrl(String fileUrl) throws AwsServiceException {
        String fileKey = fileUrl.replace(
                "https://"
                        + rekognitionProperties.getBucket()
                        + ".s3.ap-southeast-1.amazonaws.com/",
                "");

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(rekognitionProperties.getBucket())
                .key(fileKey)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    public String resolveFileKey(String fileUrl) {
        String fileKey = fileUrl.replace(
                "https://"
                        + rekognitionProperties.getBucket()
                        + ".s3.ap-southeast-1.amazonaws.com/",
                "");

        if (!ObjectUtils.isEmpty(rekognitionProperties.getCloudFrontUrl()))
            fileKey = fileKey.replace(rekognitionProperties.getCloudFrontUrl(), "");

        return fileKey;
    }

    // s3 url-г => cloud front url-р replace хийх
    private String replaceUrl(String url) {
        if (!ObjectUtils.isEmpty(rekognitionProperties.getCloudFrontUrl())
                && !ObjectUtils.isEmpty(rekognitionProperties.getCloudFrontReplaceUrl())
        )
            return url.replace(rekognitionProperties.getCloudFrontReplaceUrl(),
                    rekognitionProperties.getCloudFrontUrl());

        return url;
    }
}
