package mn.delivery.system.util.image;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

public class ImageUtil {

    private static final String EXT_JPG = "jpg"; // image compress хийхэд ашиглагдана, үндсэн EXT нөлөөлөхгүй

    public static BufferedImage resize(BufferedImage originalImage, int targetWidth, int targetHeight) {
        return resize(originalImage, targetWidth, targetHeight, Image.SCALE_DEFAULT);
    }

    public static BufferedImage resize(BufferedImage originalImage, int targetWidth, int targetHeight, int scaleHint) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, scaleHint);
        BufferedImage bufferedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return bufferedImage;
    }

    public static byte[] resizeWithCompress(InputStream inputFile, int width) throws IOException {
        return resizeWithCompress(ImageIO.read(inputFile), width);
    }

    public static byte[] resizeWithCompress(InputStream inputFile, int width, String contentType) throws IOException {
        return resizeWithCompress(ImageIO.read(inputFile), width, contentType);
    }

    public static byte[] resizeWithCompress(File inputFile, int width) throws IOException {
        return resizeWithCompress(ImageIO.read(inputFile), width);
    }

    public static byte[] resizeWithCompress(byte[] bytes, int width) throws IOException {
        return resizeWithCompress(ImageIO.read(new ByteArrayInputStream(bytes)), width);
    }

    public static byte[] resizeWithCompress(byte[] bytes, int width, String contentType) throws IOException {
        return resizeWithCompress(ImageIO.read(new ByteArrayInputStream(bytes)), width, contentType);
    }

    private static byte[] resizeWithCompress(BufferedImage read, int width) throws IOException {
        return resizeWithCompress(read, width, EXT_JPG);
    }

    private static byte[] resizeWithCompress(BufferedImage read, int width, String contentType) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedImage image = Scalr.resize(read, Scalr.Method.BALANCED, Scalr.Mode.AUTOMATIC, width);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(contentType);
        ImageWriter writer = writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(width > 1000 ? 0.6f : 0.9f);

        writer.write(null, new IIOImage(image, null, null), param);

        bos.close();
        ios.close();
        writer.dispose();
        return bos.toByteArray();
    }
}
