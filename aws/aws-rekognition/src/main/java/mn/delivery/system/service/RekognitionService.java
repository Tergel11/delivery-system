package mn.delivery.system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.dto.FaceDetectResult;
import mn.delivery.system.dto.FaceMatchResult;
import mn.delivery.system.properties.AwsRekognitionProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RekognitionService {

    private final AwsRekognitionProperties rekognitionProperties;
    private final RekognitionClient rekognitionClient;

    public DetectLabelsResponse detectLabelFromS3(String imageKey) {
        S3Object s3Object = S3Object.builder()
                .bucket(rekognitionProperties.getBucket())
                .name(imageKey)
                .build();

        return detectLabel(Image.builder()
                .s3Object(s3Object)
                .build());
    }

    private DetectLabelsResponse detectLabel(Image image) {
        DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                .image(image)
                .maxLabels(10)
                .build();

        return rekognitionClient.detectLabels(detectLabelsRequest);
//        for (Label label : detectLabelsResponse.labels()) {
//            log.info(label.name() + " - " + label.confidence());
//        }
//        return detectLabelsResponse;
    }

    public DetectModerationLabelsResponse detectModerationLabelFromS3(String imageKey) {
        S3Object s3Object = S3Object.builder()
                .bucket(rekognitionProperties.getBucket())
                .name(imageKey)
                .build();

        return detectModerationLabel(Image.builder()
                .s3Object(s3Object)
                .build());
    }

    private DetectModerationLabelsResponse detectModerationLabel(Image image) {
        DetectModerationLabelsRequest detectLabelsRequest = DetectModerationLabelsRequest.builder()
                .image(image)
                .minConfidence(60F)
                .build();

        return rekognitionClient.detectModerationLabels(detectLabelsRequest);
//        for (Label label : detectLabelsResponse.labels()) {
//            log.info(label.name() + " - " + label.confidence());
//        }
//        return detectLabelsResponse;
    }

    public FaceDetectResult detectFacesFromS3(String imageKey) {
        Image image = Image.builder()
                .s3Object(S3Object.builder()
                        .bucket(rekognitionProperties.getBucket())
                        .name(imageKey)
                        .build())
                .build();

        return detectFaces(image);
    }

    private FaceDetectResult detectFaces(Image image) {
        FaceDetectResult faceDetectResult = new FaceDetectResult();

        DetectFacesRequest detectFacesRequest = DetectFacesRequest.builder()
                .attributes(Attribute.ALL)
                .image(image)
                .build();

        DetectFacesResponse facesResponse = rekognitionClient.detectFaces(detectFacesRequest);

        List<FaceDetail> faceDetails = facesResponse.faceDetails();
        faceDetectResult.setCount(faceDetails.size());

        for (FaceDetail face : faceDetails) {
            if (!faceDetectResult.isSunglasses()) {
                faceDetectResult.setSunglasses(faceDetectResult.isSunglasses());
            }

//            AgeRange ageRange = face.ageRange();
//            log.info("The detected face is estimated to be between "
//                    + ageRange.low().toString() + " and " + ageRange.high().toString()
//                    + " years old.");
//
//            log.info("There is a smile : " + face.smile().value().toString());
        }

        return faceDetectResult;
    }

    public FaceMatchResult matchFacesFromS3(
            String sourceImageKey,
            String targetImageKey,
            float similarityThreshold) {
        Image sourceImage = Image.builder()
                .s3Object(S3Object.builder()
                        .bucket(rekognitionProperties.getBucket())
                        .name(sourceImageKey)
                        .build())
                .build();
        Image targetImage = Image.builder()
                .s3Object(S3Object.builder()
                        .bucket(rekognitionProperties.getBucket())
                        .name(targetImageKey)
                        .build())
                .build();

        return matchFaces(sourceImage, targetImage, similarityThreshold);
    }

    public FaceMatchResult matchFacesFromFile(
            String sourceImagePath,
            String targetImagePath,
            float similarityThreshold)
            throws IOException {
        try (
                InputStream sourceStream = new FileInputStream(sourceImagePath);
                InputStream targetStream = new FileInputStream(targetImagePath)
        ) {
            Image sourceImage = Image.builder()
                    .bytes(SdkBytes.fromInputStream(sourceStream))
                    .build();
            Image targetImage = Image.builder()
                    .bytes(SdkBytes.fromInputStream(targetStream))
                    .build();

            return matchFaces(sourceImage, targetImage, similarityThreshold);
        }
    }

    private FaceMatchResult matchFaces(Image sourceImage, Image targetImage, float similarityThreshold) {
        FaceMatchResult faceMatchResult = new FaceMatchResult();

        CompareFacesRequest compareFacesRequest = CompareFacesRequest.builder()
                .sourceImage(sourceImage)
                .targetImage(targetImage)
                .similarityThreshold(similarityThreshold)
                .build();

        CompareFacesResponse compareFacesResult = rekognitionClient.compareFaces(compareFacesRequest);
//        log.debug("Match count -> " + compareFacesResult.faceMatches().size());
        faceMatchResult.setMatchCount(compareFacesResult.faceMatches().size());

//        List<CompareFacesMatch> faceDetails = compareFacesResult.faceMatches();
//        for (CompareFacesMatch match : faceDetails) {
//            ComparedFace face = match.face();
//            BoundingBox position = face.boundingBox();
//            log.debug("Face at " + position.left()
//                    + " " + position.top()
//                    + " matches with " + face.confidence()
//                    + "% confidence.");
//        }

        faceMatchResult.setUnMatchCount(compareFacesResult.unmatchedFaces().size());
//        List<ComparedFace> unmatchedFaces = compareFacesResult.unmatchedFaces();
//        log.debug("There was " + unmatchedFaces.size() + " face(s) that did not match");
//        log.debug("Source image rotation: " + compareFacesResult.sourceImageOrientationCorrection());
//        log.debug("target image rotation: " + compareFacesResult.targetImageOrientationCorrection());

        return faceMatchResult;
    }
}
