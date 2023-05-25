package mn.delivery.system.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.properties.AwsS3Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AwsS3Config {

    private final AwsS3Properties awsS3Properties;

    @Bean
    public S3Client s3Client() {
        final AwsCredentialsProvider credentials = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                        this.awsS3Properties.getAccessKey(),
                        this.awsS3Properties.getSecretKey()));

        return S3Client.builder()
                .credentialsProvider(credentials)
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient() {
        final AwsCredentialsProvider credentials = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                        this.awsS3Properties.getAccessKey(),
                        this.awsS3Properties.getSecretKey()));

        return S3AsyncClient.builder()
                .credentialsProvider(credentials)
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }
}
