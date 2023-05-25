package mn.delivery.system.util;

import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.util.image.ImageUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author digz6666
 */
@Slf4j
public class UserImageUtil {

    public static final int FILE_SIZE_LIMIT = 5 * 1024 * 1024;

    private static final int PROFILE_IMAGE_WIDTH = 400;
    private static final int COVER_IMAGE_WIDTH = 1440;
    // private static final int COVER_IMAGE_HEIGHT = 480;

    public static byte[] resizeProfileImage(InputStream input) throws IOException {
        return ImageUtil.resizeWithCompress(input, PROFILE_IMAGE_WIDTH);
    }

    public static byte[] resizeCoverImage(InputStream input) throws IOException {
        return ImageUtil.resizeWithCompress(input, COVER_IMAGE_WIDTH);
    }
}
