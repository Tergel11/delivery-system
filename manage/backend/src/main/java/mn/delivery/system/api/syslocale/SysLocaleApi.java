package mn.delivery.system.api.syslocale;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.request.antd.AntdPagination;
import mn.delivery.system.api.response.antd.AntdTableDataList;
import mn.delivery.system.dao.syslocale.SysLocaleDao;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.syslanguage.SysLanguage;
import mn.delivery.system.model.syslocale.NameSpace;
import mn.delivery.system.model.syslocale.SysLocale;
import mn.delivery.system.repository.auth.UserRepository;
import mn.delivery.system.repository.syslanguage.SysLanguageRepository;
import mn.delivery.system.repository.syslocale.NameSpaceRepository;
import mn.delivery.system.repository.syslocale.SysLocaleRepository;
import mn.delivery.system.service.syslocale.SysLocaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Maroon
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@Secured("ROLE_MANAGE_DEFAULT")
@RequestMapping("/v1/locale")
public class SysLocaleApi {

    private final UserRepository userRepository;
    private final SysLocaleRepository sysLocaleRepository;
    private final SysLocaleDao sysLocaleDao;
    private final SysLocaleService sysLocaleService;
    private final NameSpaceRepository nameSpaceRepository;
    private final SysLanguageRepository sysLanguageRepository;

    @GetMapping
    @Secured("ROLE_LOCALE_VIEW")
    public ResponseEntity<?> list(String nsId, String lng, String field, String translation, Boolean active, AntdPagination pagination) {
        AntdTableDataList<SysLocale> listData = new AntdTableDataList<>();

        pagination.setTotal(sysLocaleDao.count(nsId, lng, field, translation, active));
        listData.setPagination(pagination);
        listData.setList(sysLocaleService.list(nsId, lng, field, translation, active, pagination.toPageRequest()));

        return ResponseEntity.ok(listData);
    }

    @GetMapping("select")
    public ResponseEntity<?> select(String nsId, String lng, String field, String translation, Boolean active) {

        Iterable<SysLocale> listData = sysLocaleDao.list(nsId, lng, field, translation, active, null);
        return ResponseEntity.ok(listData);
    }

    @PostMapping("create")
    @Secured("ROLE_LOCALE_MANAGE")
    public ResponseEntity<?> create(@RequestBody List<SysLocale> createRequests, Principal principal) {

        User user = userRepository.findByEmailAndDeletedFalse(principal.getName());
        if (user != null) {
            for (SysLocale item : createRequests) {

                log.debug("creating locale -> " + item);

                if (ObjectUtils.isEmpty(item.getTranslation())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(item.getField() + item.getLng() + " утга Хоосон байна.");
                }

                if (sysLocaleRepository.existsByFieldAndLng(item.getField(), item.getLng())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(item.getField() + item.getLng() + " орчуулга бүртгэсэн байна.");
                }
                item.setCreatedBy(user.getId());
                item.setCreatedDate(LocalDateTime.now());
            }
            sysLocaleRepository.saveAll(createRequests);

            return ResponseEntity.ok(true);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }
    }

    @PostMapping("update")
    @Secured("ROLE_LOCALE_MANAGE")
    public ResponseEntity<?> update(@RequestBody SysLocale updateRequest, Principal principal) {
        log.debug("updating locale -> " + updateRequest);

        if (ObjectUtils.isEmpty(updateRequest.getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID хоосон байна.");

        User user = userRepository.findByEmailAndDeletedFalse(principal.getName());

        if (user != null) {
            Optional<SysLocale> typeOptional = sysLocaleRepository.findById(updateRequest.getId());

            if (typeOptional.isPresent()) {
                SysLocale locale = typeOptional.get();

                locale.setField(updateRequest.getField());
                locale.setLng(updateRequest.getLng());
                locale.setCode(updateRequest.getCode());
                locale.setTranslation(updateRequest.getTranslation());
                locale.setActive(updateRequest.isActive());
                locale.setModifiedBy(user.getId());
                locale.setModifiedDate(LocalDateTime.now());

                locale = sysLocaleRepository.save(locale);

                return ResponseEntity.ok(locale.getId());

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Locale not found.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return ResponseEntity.ok().body(sysLocaleRepository.findById(id).orElse(null));
    }

    @GetMapping("get-with-code/{code}")
    public ResponseEntity<?> getWithCode(@PathVariable String code) {
        return ResponseEntity.ok().body(sysLocaleRepository.findByCode(code).orElse(null));
    }

    @PostMapping("delete")
    @Secured("ROLE_LOCALE_MANAGE")
    public ResponseEntity<?> delete(@RequestParam String id, Principal principal) {
        log.debug("delete id ->  " + id);

        User user = userRepository.findByEmailAndDeletedFalse(principal.getName());
        if (user != null) {
            Optional<SysLocale> typeOptional = sysLocaleRepository.findById(id);
            if (typeOptional.isPresent()) {
                SysLocale locale = typeOptional.get();
                locale.setDeleted(true);
                locale.setActive(false);
                locale.setModifiedDate(LocalDateTime.now());
                locale.setModifiedBy(user.getId());

                sysLocaleRepository.save(locale);
                return ResponseEntity.ok().body(true);
            } else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Locale not found.");
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
    }

    @GetMapping("get-locale/{lng}/{ns}")
    public ResponseEntity<?> getLocale(@PathVariable String lng, @PathVariable String ns) {

        log.debug("ns ==> " + ns);
        log.debug("lng ==> " + lng);

        NameSpace nameSpace = nameSpaceRepository.findByValue(ns).orElse(null);
        if (ObjectUtils.isEmpty(nameSpace))
            return ResponseEntity.badRequest().body("Name space олдсонгүй.");

        SysLanguage sysLanguage = sysLanguageRepository.findByCode(lng).orElse(null);
        if (ObjectUtils.isEmpty(sysLanguage))
            return ResponseEntity.badRequest().body("Системийн хэл олдсонгүй.");

        Map<String, String> listData = new HashMap<>();
        Iterable<SysLocale> locales = sysLocaleDao.list(ns, lng, null, null, true, null);

        for (SysLocale locale : locales){
            listData.put(locale.getField(), locale.getTranslation());
        }

        return ResponseEntity.ok().body(listData);
    }

}
