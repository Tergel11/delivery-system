package mn.delivery.system.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author digz6666
 */
public class UUIDGenerator {

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static final Pattern UUID_REGEX = Pattern.compile(
        "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");

    public static UUID generateType1UUID() {

        long most64SigBits = get64MostSignificantBitsForVersion1();
        long least64SigBits = get64LeastSignificantBitsForVersion1();

        return new UUID(most64SigBits, least64SigBits);
    }

    private static long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong + variant3BitFlag;
    }

    private static long get64MostSignificantBitsForVersion1() {
        LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
        Duration duration = Duration.between(start, LocalDateTime.now());
        long seconds = duration.getSeconds();
        long nanos = duration.getNano();
        long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
        long least12SignificantBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
        long version = 1 << 12;
        return (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version
            + least12SignificantBitOfTime;
    }

    public static UUID generateType3UUID(String namespace, String name)
        throws UnsupportedEncodingException {

        byte[] nameSpaceBytes = bytesFromUUID(namespace);
        byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
        byte[] result = joinBytes(nameSpaceBytes, nameBytes);

        return UUID.nameUUIDFromBytes(result);
    }

    public static UUID generateType4UUID() {
        return UUID.randomUUID();
    }

    public static UUID generateType5UUID(String namespace, String name)
        throws UnsupportedEncodingException {

        byte[] nameSpaceBytes = bytesFromUUID(namespace);
        byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
        byte[] result = joinBytes(nameSpaceBytes, nameBytes);

        return type5UUIDFromBytes(result);
    }

    public static UUID type5UUIDFromBytes(byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("SHA-256 not supported", nsae);
        }
        byte[] bytes = Arrays.copyOfRange(md.digest(name), 0, 16);
        bytes[6] &= 0x0f;
        bytes[6] |= 0x50;
        bytes[8] &= 0x3f;
        bytes[8] |= 0x80;
        return constructType5UUID(bytes);
    }

    private static UUID constructType5UUID(byte[] data) {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16 : "data must be 16 bytes in length";

        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (data[i] & 0xff);
        }

        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (data[i] & 0xff);
        }
        return new UUID(msb, lsb);
    }

    public static String generateUniqueKeysWithUUIDAndMessageDigest()
        throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest salt = MessageDigest.getInstance("SHA-256");
        salt.update(UUID.randomUUID()
            .toString()
            .getBytes(StandardCharsets.UTF_8));
        return bytesToHex(salt.digest());
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static byte[] bytesFromUUID(String uuidHexString) {
        String normalizedUUIDHexString = uuidHexString.replace("-", "");

        assert normalizedUUIDHexString.length() == 32;

        byte[] bytes = new byte[16];
        for (int i = 0; i < 16; i++) {
            byte b = hexToByte(normalizedUUIDHexString.substring(i * 2, i * 2 + 2));
            bytes[i] = b;
        }
        return bytes;
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = Character.digit(hexString.charAt(0), 16);
        int secondDigit = Character.digit(hexString.charAt(1), 16);
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    public static byte[] joinBytes(byte[] byteArray1, byte[] byteArray2) {
        int finalLength = byteArray1.length + byteArray2.length;
        byte[] result = new byte[finalLength];

        for (int i = 0; i < byteArray1.length; i++) {
            result[i] = byteArray1[i];
        }

        for (int i = 0; i < byteArray2.length; i++) {
            result[byteArray1.length + i] = byteArray2[i];
        }

        return result;
    }

    public static UUID generateType5UUID(String name) {
        try {

            byte[] bytes = name.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(bytes);

            long msb = getLeastAndMostSignificantBitsVersion5(hash, 0);
            long lsb = getLeastAndMostSignificantBitsVersion5(hash, 8);
            msb &= ~(0xfL << 12);
            msb |= ((long) 5) << 12;
            lsb &= ~(0x3L << 62);
            lsb |= 2L << 62;
            return new UUID(msb, lsb);

        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    public static boolean isValidUUID(String name) {
        if (!StringUtils.hasText(name)) {
            return false;
        }
        Matcher matcher = UUID_REGEX.matcher(name);
        return matcher.find();
    }

    private static long getLeastAndMostSignificantBitsVersion5(final byte[] src, final int offset) {
        long ans = 0;
        for (int i = offset + 7; i >= offset; i -= 1) {
            ans <<= 8;
            ans |= src[i] & 0xffL;
        }
        return ans;
    }
}
