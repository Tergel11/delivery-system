package mn.delivery.system.api.systemconfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.BaseController;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.systemconfig.SystemCron;
import mn.delivery.system.model.systemconfig.enums.SystemCronType;
import mn.delivery.system.repository.systemconfig.SystemCronRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/v1/system-cron")
@Secured("ROLE_MANAGE_DEFAULT")
@RequiredArgsConstructor
public class SystemCronApi extends BaseController {
    private final SystemCronRepository systemCronRepository;

    @GetMapping("all")
    public ResponseEntity<?> all(Boolean enable, Principal principal, Locale locale) {
        try {
            if (enable == null)
                return errorInvalidRequest(locale);

            User user = basePermissionService.user(principal, locale);
            log.info("all cron enable: " + enable + ", by: " + user.getId());
            List<SystemCron> crons = systemCronRepository.findAllByCronTypeIn(Arrays.asList(SystemCronType.values()));
            if (crons != null) {
                for (SystemCron cron: crons)
                    cron.setEnabled(enable);
                systemCronRepository.saveAll(crons);
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return serverError(e.getMessage());
        }
    }

    @GetMapping("payment-cron")
    public ResponseEntity<?> cron(Boolean enable, Principal principal, Locale locale) {
        try {
            if (enable == null)
                return errorInvalidRequest(locale);

            User user = basePermissionService.user(principal, locale);
            log.info("payment cron enable: " + enable + ", by: " + user.getId());
            List<SystemCron> crons = systemCronRepository.findAllByCronTypeIn(SystemCronType.getPaymentCronTypes());
            if (crons != null) {
                for (SystemCron cron: crons)
                    cron.setEnabled(enable);
                systemCronRepository.saveAll(crons);
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return serverError(e.getMessage());
        }
    }

    @GetMapping("payment-cron-golomt")
    public ResponseEntity<?> golomt(Boolean enable, Principal principal, Locale locale) {
        try {
            if (enable == null)
                return errorInvalidRequest(locale);

            User user = basePermissionService.user(principal, locale);
            log.info("payment cron golomt enable: " + enable + ", by: " + user.getId());
            List<SystemCron> crons = systemCronRepository.findAllByCronTypeIn(SystemCronType.getPaymentGolomtCronTypes());
            if (crons != null) {
                for (SystemCron cron: crons)
                    cron.setEnabled(enable);
                systemCronRepository.saveAll(crons);
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return serverError(e.getMessage());
        }
    }

    @GetMapping("payment-cron-tdb")
    public ResponseEntity<?> tdb(Boolean enable, Principal principal, Locale locale) {
        try {
            if (enable == null)
                return errorInvalidRequest(locale);

            User user = basePermissionService.user(principal, locale);
            log.info("payment cron tdb enable: " + enable + ", by: " + user.getId());
            List<SystemCron> crons = systemCronRepository.findAllByCronTypeIn(SystemCronType.getPaymentTdbCronTypes());
            if (crons != null) {
                for (SystemCron cron: crons)
                    cron.setEnabled(enable);
                systemCronRepository.saveAll(crons);
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return serverError(e.getMessage());
        }
    }

    @GetMapping("shared-cron")
    public ResponseEntity<?> shared(Boolean enable, Principal principal, Locale locale) {
        try {
            if (enable == null)
                return errorInvalidRequest(locale);

            User user = basePermissionService.user(principal, locale);
            log.info("shared cron enable: " + enable + ", by: " + user.getId());
            List<SystemCron> crons = systemCronRepository.findAllByCronTypeIn(SystemCronType.getSharedCronTypes());
            if (crons != null) {
                for (SystemCron cron: crons)
                    cron.setEnabled(enable);
                systemCronRepository.saveAll(crons);
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return serverError(e.getMessage());
        }
    }

}
