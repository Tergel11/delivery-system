package mn.delivery.system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.properties.AwsS3Properties;
import mn.delivery.system.s3.ImageType;
import mn.delivery.system.dto.MoveFileRequest;
import mn.delivery.system.exception.AwsException;
import mn.delivery.system.exception.ContentTypeException;
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
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3BucketService {

    private final AwsS3Properties s3Properties;

    private S3Client s3Client;

    private S3Utilities s3Utilities;

    private S3Presigner s3Presigner;

    @PostConstruct
    private void init() {
        log.info("Initializing s3 bucket service with: " + s3Properties.getAccessKey());
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                s3Properties.getAccessKey(),
                s3Properties.getSecretKey());
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCreds);

        s3Client = S3Client.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(credentialsProvider)
                .build();

        s3Utilities = S3Utilities.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();

        s3Presigner = S3Presigner.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public String upload(
            String fileKey,
            String name,
            String contentType,
            byte[] file,
            long contentLength,
            Map<String, String> metaData
    ) throws AwsServiceException, IOException {
        return uploadStream(
                fileKey,
                name,
                contentType,
                new ByteArrayInputStream(file),
                contentLength,
                metaData
        );
    }

    public String uploadStream(
            String fileKey,
            String name,
            String contentType,
            InputStream stream,
            long contentLength,
            Map<String, String> metaData
    ) throws AwsServiceException, IOException {
        try {
            log.info(fileKey + " -> uploading file + " + name + " to bucket: " + s3Properties.getBucket());
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(fileKey)
                    .contentType(contentType)
                    .metadata(metaData)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            // TODO append file extension
            PutObjectResponse putResponse = s3Client.putObject(
                    objectRequest,
                    RequestBody.fromInputStream(stream, contentLength)
            );
            log.info(fileKey + " -> file uploaded with etag: " + putResponse.eTag());

            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(fileKey)
                    .build();
            return replaceUrl(s3Utilities.getUrl(getUrlRequest).toString());
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        } finally {
            stream.close();
        }
    }

    // entity-с хамаарч зургийг compress болон resize хийнэ
    public String uploadStream(
            String fileKey,
            String name,
            String contentType,
            byte[] bytes,
            String entity,
            Map<String, String> metaData
    ) throws AwsServiceException, IOException, ContentTypeException {
        log.info("{} -> uploading file {} to bucket: {}", fileKey, name, s3Properties.getBucket());

        ImageType imageType = ImageTypeResolver.resolve(entity);
        ImageService.checkContentType(imageType, contentType);
        try {
            bytes = ImageService.resize(bytes, imageType, contentType);

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
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
                    .bucket(s3Properties.getBucket())
                    .key(fileKey)
                    .build();
            return replaceUrl(s3Utilities.getUrl(getUrlRequest).toString());
        } catch (Exception ex) {
            log.error("upload error: {}", ex.getMessage(), ex);
            throw new IOException(ex.getMessage());
        }
    }

    public boolean ifExists(String fileKey) {
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Properties.getBucket())
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
                .bucket(s3Properties.getBucket())
                .key(fileKey)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    public void deleteFileIfExists(String fileKey) throws AwsServiceException {
        log.info("delete file : " + fileKey);
        if (ifExists(fileKey)) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(fileKey)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        }
    }

    public void deleteFileByUrl(String fileUrl) throws AwsServiceException {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(resolveFileKey(fileUrl))
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    public String moveTempFile(MoveFileRequest request) throws AwsServiceException, AwsException {
        log.info("move request: " + request);

        String entity = request.getEntity();
        String sourceFileKey = resolveFileKey(request.getFileUrl());

        String newFileName = request.getFileName(sourceFileKey.replace(S3BucketFolder.TEMP, ""));

        String targetFileKey = (ObjectUtils.isEmpty(entity) ? "" : entity) + newFileName;

        log.info("sourceFileKey: " + sourceFileKey);
        log.info("fileName: " + newFileName);
        log.info("bucket: " + s3Properties.getBucket());

        if (!ifExists(sourceFileKey))
            throw new AwsException("Файл олдсонгүй");

        s3Client.copyObject(CopyObjectRequest
                .builder()
                .sourceBucket(s3Properties.getBucket())
                .sourceKey(sourceFileKey)
                .destinationBucket(s3Properties.getBucket())
                .destinationKey(targetFileKey)
                .metadata(request.getMetaData(targetFileKey, newFileName))
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build());

        if (!request.isCopy())
            deleteFile(sourceFileKey);

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(targetFileKey)
                .build();
        return replaceUrl(s3Utilities.getUrl(getUrlRequest).toString());
    }

    public String getPresignedUrl(String fileKey) throws AwsServiceException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(fileKey)
                .build();

        // Create a GetObjectPresignRequest to specify the signature duration
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner
                .presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();
    }

    public String resolveFileKey(String fileUrl) {
        String fileKey = fileUrl.replace(
                "https://"
                        + s3Properties.getBucket()
                        + ".s3.ap-southeast-1.amazonaws.com/",
                "");
        if (!ObjectUtils.isEmpty(s3Properties.getCloudFrontUrl()))
            fileKey = fileKey.replace(s3Properties.getCloudFrontUrl(), "");

        return fileKey;
    }

    // s3 url-г => cloud front url-р replace хийх
    private String replaceUrl(String url) {
        if (!ObjectUtils.isEmpty(s3Properties.getCloudFrontUrl())
                && !ObjectUtils.isEmpty(s3Properties.getCloudFrontReplaceUrl())
        )
            return url.replace(s3Properties.getCloudFrontReplaceUrl(),
                    s3Properties.getCloudFrontUrl());

        return url;
    }
}
