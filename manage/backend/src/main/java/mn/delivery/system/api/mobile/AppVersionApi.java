package mn.delivery.system.api.mobile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.request.AppVersionUpdateRequest;
import mn.delivery.system.api.BaseController;
import mn.delivery.system.model.systemconfig.SystemKey;
import mn.delivery.system.model.systemconfig.SystemKeyValue;
import mn.delivery.system.repository.systemconfig.SystemKeyValueRepository;
import mn.delivery.system.service.systemconfig.SystemKeyValueService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/app-version")
@RequiredArgsConstructor
@Secured({"ROLE_CUSTOMER"})
public class AppVersionApi extends BaseController {
    private final SystemKeyValueRepository systemKeyValueRepository;
    private final SystemKeyValueService systemKeyValueService;

    @PostMapping("ios")
    public ResponseEntity<?> ios(@Valid @RequestBody AppVersionUpdateRequest updateRequest) {
        SystemKeyValue keyValue = systemKeyValueService.get(SystemKey.APP_VERSION_IOS);
        if (ObjectUtils.isEmpty(keyValue))
            return badRequest("App version мэдээлэл олдсонгүй");

        keyValue.setValue(updateRequest.getVersion());
        systemKeyValueRepository.save(keyValue);
        return ResponseEntity.ok(keyValue);
    }

    @PostMapping("android")
    public ResponseEntity<?> android(@Valid @RequestBody AppVersionUpdateRequest updateRequest) {
        SystemKeyValue keyValue = systemKeyValueService.get(SystemKey.APP_VERSION_ANDROID);
        if (ObjectUtils.isEmpty(keyValue))
            return badRequest("App version мэдээлэл олдсонгүй");

        keyValue.setValue(updateRequest.getVersion());
        systemKeyValueRepository.save(keyValue);
        return ResponseEntity.ok(keyValue);
    }

}
