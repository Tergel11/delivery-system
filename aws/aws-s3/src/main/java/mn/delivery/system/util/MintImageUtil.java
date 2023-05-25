package mn.delivery.system.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import mn.delivery.system.util.image.Scalr;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tergel
 */
@Slf4j
@Service
public class MintImageUtil {

    public static final int FILE_SIZE_MB = 1024 * 1024;
    public static final List<String> IMAGE_EXTENSIONS = List.of("image/jpeg", "image/jpg", "image/gif", "image/png");
    private static final String EXT_JPG = "jpg"; // image compress хийхэд ашиглагдана, үндсэн EXT нөлөөлөхгүй
    private static final int MAIN_IMAGE_WIDTH = 800;
    private static final int THUMB_IMAGE_WIDTH = 400;

    public static byte[] resizeImageWithCompress(InputStream inputFile, int width) throws Exception {
        return resizeWithCompress(ImageIO.read(inputFile), width);
    }

    public static byte[] resizeImageWithCompress(File inputFile, int width) throws IOException {
        return resizeWithCompress(ImageIO.read(inputFile), width);
    }

    public byte[] nftImagesUpload(byte[] inputFileByte, String key) throws IOException {
        return resizeWithCompressForNft(ImageIO.read(new ByteArrayInputStream(inputFileByte)), key);
    }

    private static byte[] resizeImage(int width, BufferedImage read) throws IOException {
        BufferedImage bo = Scalr.resize(read, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, width);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bo, EXT_JPG, bos);
        bos.close();
        return bos.toByteArray();
    }

    private static byte[] resizeWithCompress(BufferedImage read, int width) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedImage image = Scalr.resize(read, Scalr.Method.BALANCED, Scalr.Mode.FIT_TO_WIDTH, width);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(EXT_JPG);
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

    private byte[] resizeWithCompressForNft(BufferedImage read, String key) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedImage image = Scalr.resize(read, Scalr.Method.BALANCED,
                read.getHeight() > read.getWidth() ? Scalr.Mode.FIT_TO_HEIGHT : Scalr.Mode.FIT_TO_WIDTH,
                key.equals("main") ? MAIN_IMAGE_WIDTH : THUMB_IMAGE_WIDTH);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(EXT_JPG);
        ImageWriter writer = writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(key.equals("main") ? 0.9f : 0.6f);

        writer.write(null, new IIOImage(image, null, null), param);

        bos.close();
        ios.close();
        writer.dispose();
        return bos.toByteArray();
    }
}
