package mn.delivery.system.s3;

import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

/**
 * @author Tergel
 */
public class ImageConfig {

    // resize хийх width хэмжээ
    public static final Map<ImageType, Integer> ImageWidthResize = Map.of(
            ImageType.USER_PROFILE, 400,
            ImageType.USER_COVER, 1440,
            ImageType.KYC_ID_CARD_FRONT, 800,
            ImageType.KYC_ID_CARD_BACK, 800,
            ImageType.KYC_SELF, 800
    );

    // file content type
    public static final List<String> ImageContentTypes = List.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            "image/jpg"
    );

    // content type
    public static final Map<ImageType, List<String>> FileContentType = Map.of(
            ImageType.USER_PROFILE, ImageContentTypes,
            ImageType.USER_COVER, ImageContentTypes,
            ImageType.KYC_ID_CARD_FRONT, ImageContentTypes,
            ImageType.KYC_ID_CARD_BACK, ImageContentTypes,
            ImageType.KYC_SELF, ImageContentTypes
    );

    public static final Map<ImageType, String> FileContentTypeError = Map.of(
            ImageType.USER_PROFILE, "Зураг оруулна уу",
            ImageType.USER_COVER, "Зураг оруулна уу",
            ImageType.KYC_ID_CARD_FRONT, "Зураг оруулна уу",
            ImageType.KYC_ID_CARD_BACK, "Зураг оруулна уу",
            ImageType.KYC_SELF, "Зураг оруулна уу"
    );
}
