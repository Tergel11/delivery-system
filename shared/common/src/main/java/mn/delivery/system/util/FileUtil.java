package mn.delivery.system.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileUtil {

    public static Set<String> listFiles(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public static Map<String, String> mapFiles(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .collect(Collectors.toMap(File::getName, null));
    }

    public static boolean createDirectory(String filePath) {
        boolean result = false;

        File file = new File(filePath);
        if (!file.exists()) {
            result = file.mkdirs();
        } else if (file.isDirectory()) {
            result = true;
        }

        if (!result) {
            log.debug("Create directory failed: " + filePath);
        }
        return result;
    }

    public static boolean createDirectory(String rootPath, String folderPath) {
        boolean result = false;

        File file = new File(rootPath + "/" + folderPath);
        if (!file.exists()) {
            result = file.mkdirs();
            //file.setReadable(true);
            //file.setExecutable(true);
            fixPathPermissions(rootPath, folderPath);
        } else if (file.isDirectory()) {
            result = true;
        }

        return result;
    }

    public static boolean createFile(String rootPath, String folderPath, String fileName, byte[] bytes)
            throws IOException {
        boolean result = false;

        boolean directoryCreated = createDirectory(rootPath, folderPath);
        if (directoryCreated) {
            boolean fileDeleted = false;
            File file = new File(rootPath + "/" + folderPath + "/" + fileName);
            if (file.exists()) {
                fileDeleted = file.delete();
            }

            if (!file.exists() || fileDeleted) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(bytes);
                }

                fixPathPermission(rootPath + "/" + folderPath + "/" + fileName);
                result = true;
            }
        }

        return result;
    }

    public static void fetchFiles(File dir, Consumer<File> fileConsumer) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    fetchFiles(file, fileConsumer);
                }
            }
        } else {
            fileConsumer.accept(dir);
        }
    }

    public static void fetchEmptyDirectories(File dir, Consumer<File> fileConsumer) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                // empty dir
                fileConsumer.accept(dir);
            } else if (dir.isDirectory()) {
                File[] subDirs = dir.listFiles();
                if (subDirs != null) {
                    for (File file : files) {
                        fetchEmptyDirectories(file, fileConsumer);
                    }
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        if (file.exists() && file.canRead()) {
            return file.delete();
        }
        return false;
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.canRead()) {
            return file.delete();
        }
        return false;
    }

    public static boolean isExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static void copyFile(String inputPath, String outputPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File(outputPath), true)) {
            Files.copy(new File(inputPath).toPath(), fos);
        }
    }

    public static void fixPathPermissions(String rootPath, String folderPath) {
        Path path = Paths.get(folderPath);

        String _curPath = "" + rootPath;
        for (Path _path : path) {
            _curPath += "/" + _path.toString();
            fixPathPermission(_curPath);
        }
    }

    public static void fixPathPermission(String pathStr) {
        try {
            Path path = Paths.get(pathStr);

            Set<PosixFilePermission> perms = Files.readAttributes(path, PosixFileAttributes.class).permissions();
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            perms.add(PosixFilePermission.GROUP_WRITE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            perms.add(PosixFilePermission.OTHERS_WRITE);
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);
            Files.setPosixFilePermissions(path, perms);
        } catch (UnsupportedOperationException | IOException e) {
        }
    }

    public static Resource loadFileAsResource(String rootPath, String uploadPath, String fileName) throws Exception {
        try {
            Path filePath = Paths.get(rootPath + "/" + uploadPath).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found");
            }
        } catch (MalformedURLException ex) {
            throw new Exception("File not found");
        }
    }

    public static String getFileUrl(String url, String rootUrl) {
        return url.replaceAll(rootUrl, "");
    }

    public static String getExtensionFromUrl(String url) {
        return ObjectUtils.isEmpty(url) ? null : url.substring(url.lastIndexOf(".") + 1).toLowerCase();
    }
}
