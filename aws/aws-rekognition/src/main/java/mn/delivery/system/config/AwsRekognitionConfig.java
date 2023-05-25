package mn.delivery.system.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.properties.AwsRekognitionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AwsRekognitionConfig {

    private final AwsRekognitionProperties rekognitionProperties;

    @Bean
    public RekognitionClient rekognitionClient() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                rekognitionProperties.getAccessKey(),
                rekognitionProperties.getSecretKey());
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCreds);

        return RekognitionClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
