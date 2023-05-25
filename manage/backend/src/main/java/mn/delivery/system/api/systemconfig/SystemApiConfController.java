package mn.delivery.system.api.systemconfig;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.BaseController;
import mn.delivery.system.api.request.system.ModuleToggleRequest;
import mn.delivery.system.model.systemconfig.SystemApiConf;
import mn.delivery.system.repository.systemconfig.SystemApiConfRepository;
import mn.delivery.system.util.LocalizationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/system-api")
@Secured("ROLE_MANAGE_DEFAULT")
@RequiredArgsConstructor
public class SystemApiConfController extends BaseController {

    private final LocalizationUtil localizationUtil;
    private final SystemApiConfRepository systemApiConfRepository;

    @PostMapping("toggle-module")
    public ResponseEntity<?> toggleModule(
        @Valid @RequestBody ModuleToggleRequest request) {
        try {
            final var moduleName = request.getModuleType();
            final var existingModule = systemApiConfRepository.findByModuleType(
                moduleName);
            SystemApiConf conf;
            if (existingModule.isPresent()) {
                conf = existingModule.get();
                conf.setEnabled(!conf.isEnabled());
            } else {
                conf = new SystemApiConf();
                conf.setEnabled(false);
                conf.setModuleType(moduleName);
            }
            return ResponseEntity.ok(systemApiConfRepository.save(conf));
        } catch (Exception e) {
            return serverError(e.getMessage());
        }
    }
}
