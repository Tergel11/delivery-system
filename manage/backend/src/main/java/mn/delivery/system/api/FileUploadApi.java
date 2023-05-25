package mn.delivery.system.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.constants.GlobalDateFormat;
import mn.delivery.system.dto.MoveFileRequest;
import mn.delivery.system.exception.permission.PermissionException;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.service.S3BucketFolder;
import mn.delivery.system.service.S3BucketService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/file")
@RequiredArgsConstructor
@Secured({"ROLE_CUSTOMER", "ROLE_MANAGE_DEFAULT"})
public class FileUploadApi extends BaseController {
    private final S3BucketService s3BucketService;

    @PostMapping(value = "upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) String entity,
            @RequestParam(required = false, defaultValue = "true") Boolean temp,
            Principal principal,
            Locale locale
    ) {
        if (ObjectUtils.isEmpty(file))
            return badRequest("Файл оруулна уу");

        try {
            User user = basePermissionService.user(principal, locale);

            StringBuilder fileKeyBuilder = new StringBuilder();
            StringBuilder fileNameBuilder = new StringBuilder();
            if (temp) {
                fileKeyBuilder.append(S3BucketFolder.TEMP);
                fileNameBuilder
                        .append(GlobalDateFormat.DATE_TIME_FILE.format(LocalDateTime.now()))
                        .append("_")
                        .append(file.getOriginalFilename());
            } else {
                if (!ObjectUtils.isEmpty(entity)) {
                    fileKeyBuilder.append(entity);
                    if (!entity.endsWith("/"))
                        fileKeyBuilder.append("/");
                }
                fileNameBuilder.append(file.getOriginalFilename());
            }
            fileKeyBuilder.append(fileNameBuilder);

            String url = s3BucketService.upload(
                    fileKeyBuilder.toString(),
                    fileNameBuilder.toString(),
                    file.getContentType(),
                    file.getBytes(),
                    file.getSize(),
                    Map.of(
                            "userId", user.getId(),
                            "key", fileKeyBuilder.toString(),
                            "name", fileNameBuilder.toString(),
                            "entity", ObjectUtils.isEmpty(entity) ? "" : entity
                    )
            );

            if (ObjectUtils.isEmpty(url))
                return serverError("Зураг оруулахад алдаа гарлаа");

            return ResponseEntity.ok(url);
        } catch (PermissionException e) {
            return errorPermission(locale);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("file upload entity: " + entity + ", error: " + e);
            return serverError("Системийн алдаа гарлаа");
        }
    }

    @PostMapping(value = "upload-move", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadTemp(
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) String entity,
            @RequestParam(required = false, defaultValue = "true") Boolean temp,
            @RequestParam(required = false) String moveKey,
            @RequestParam(required = false) String moveName,
            Principal principal,
            Locale locale
    ) {
        if (ObjectUtils.isEmpty(file))
            return badRequest("Файл оруулна уу");

        try {
            User user = basePermissionService.user(principal, locale);

            StringBuilder fileKeyBuilder = new StringBuilder();
            StringBuilder fileNameBuilder = new StringBuilder();
            if (temp) {
                fileKeyBuilder.append(S3BucketFolder.TEMP);
                fileNameBuilder
                        .append(GlobalDateFormat.DATE_TIME_FILE.format(LocalDateTime.now()))
                        .append("_")
                        .append(file.getOriginalFilename());
            } else {
                if (!ObjectUtils.isEmpty(entity)) {
                    fileKeyBuilder.append(entity);
                    if (!entity.endsWith("/"))
                        fileKeyBuilder.append("/");
                }
                fileNameBuilder.append(file.getOriginalFilename());
            }
            fileKeyBuilder.append(fileNameBuilder);

            String url = s3BucketService.upload(
                    fileKeyBuilder.toString(),
                    fileNameBuilder.toString(),
                    file.getContentType(),
                    file.getBytes(),
                    file.getSize(),
                    Map.of(
                            "userId", user.getId(),
                            "key", fileKeyBuilder.toString(),
                            "name", fileNameBuilder.toString(),
                            "entity", ObjectUtils.isEmpty(entity) ? "" : entity
                    )
            );

            if (ObjectUtils.isEmpty(url))
                return serverError("Зураг оруулахад алдаа гарлаа");

            MoveFileRequest mfRequestThumb = MoveFileRequest.builder()
                    .entity(moveKey)
                    .dataId("thumb")
                    .name(moveName)
                    .fileUrl(url)
                    .build();
            String newUrl = s3BucketService.moveTempFile(mfRequestThumb);

            return ResponseEntity.ok(Map.of("url", url, "movedUrl", newUrl));
        } catch (PermissionException e) {
            return errorPermission(locale);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("file upload entity: " + entity + ", error: " + e);
            return serverError("Системийн алдаа гарлаа");
        }
    }
}
