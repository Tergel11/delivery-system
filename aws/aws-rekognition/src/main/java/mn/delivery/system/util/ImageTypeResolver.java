package mn.delivery.system.util;

import mn.delivery.system.s3.ImageType;
import mn.delivery.system.service.S3BucketFolder;
import org.springframework.util.ObjectUtils;

import java.util.Map;

public class ImageTypeResolver {

    private static final Map<String, ImageType> typeMap = Map.of(
            S3BucketFolder.KYC_ID_CARD_FRONT, ImageType.KYC_ID_CARD_FRONT,
            S3BucketFolder.KYC_ID_CARD_BACK, ImageType.KYC_ID_CARD_BACK,
            S3BucketFolder.KYC_SELF, ImageType.KYC_SELF
    );

    public static ImageType resolve(String entity) {
        if (ObjectUtils.isEmpty(entity))
            return null;

        return typeMap.get(entity);
    }
}
