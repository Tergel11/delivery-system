package mn.delivery.system.util;

import mn.delivery.system.s3.ImageType;
import org.springframework.util.ObjectUtils;

import java.util.Map;

public class ImageTypeResolver {

    public static final String USER_PROFILE = "user/profile"; // user kyc id card зураг
    public static final String USER_COVER = "user/cover"; // user kyc id card зураг
    public static final String KYC_ID_CARD_FRONT = "kyc/card-front/"; // user kyc id card зураг
    public static final String KYC_ID_CARD_BACK = "kyc/card-back/"; // user kyc id card зураг
    public static final String KYC_SELF = "kyc/self/"; // user kyc selfie

    private static final Map<String, ImageType> typeMap = Map.of(
            USER_PROFILE, ImageType.USER_PROFILE,
            USER_COVER, ImageType.USER_COVER,
            KYC_ID_CARD_FRONT, ImageType.KYC_ID_CARD_FRONT,
            KYC_ID_CARD_BACK, ImageType.KYC_ID_CARD_BACK,
            KYC_SELF, ImageType.KYC_SELF
    );

    public static ImageType resolve(String entity) {
        if (ObjectUtils.isEmpty(entity))
            return null;

        return typeMap.get(entity);
    }
}
