package mn.delivery.system.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "aws.rekognition")
public class AwsRekognitionProperties {

    private String accessKey;
    private String secretKey;
    private String bucket;

    private String cloudFrontUrl;
    private String cloudFrontReplaceUrl;
}
