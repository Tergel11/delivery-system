package mn.delivery.system.api.syslanguage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.request.antd.AntdPagination;
import mn.delivery.system.api.response.antd.AntdTableDataList;
import mn.delivery.system.dao.syslanguage.SysLanguageDao;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.syslanguage.SysLanguage;
import mn.delivery.system.repository.auth.UserRepository;
import mn.delivery.system.repository.syslanguage.SysLanguageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@Secured("ROLE_MANAGE_DEFAULT")
@RequestMapping("/v1/sys-language")
public class SysLanguageApi {

    private final UserRepository userRepository;
    private final SysLanguageRepository sysLanguageRepository;
    private final SysLanguageDao sysLanguageDao;

    @GetMapping
    @Secured("ROLE_LOCALE_VIEW")
    public ResponseEntity<?> list(String name, String code, Boolean active, AntdPagination pagination) {
//        log.debug("Reference type list -> name: " + name + ", deleted: " + deleted + ", pagination: " + pagination);
        AntdTableDataList<SysLanguage> listData = new AntdTableDataList<>();

        pagination.setTotal(sysLanguageDao.count(name, code, active));
        listData.setPagination(pagination);
        listData.setList(sysLanguageDao.list(name, code, active,
                PageRequest.of(pagination.getCurrentPage(), pagination.getPageSize(),
                        Sort.by(Sort.Direction.fromString("DESC"), "order")
                )));

        return ResponseEntity.ok(listData);
    }

    @GetMapping("select")
    public ResponseEntity<?> select(String name, String code, Boolean active) {

        Iterable<SysLanguage> listData = sysLanguageDao.list(name, code, active, null);
        return ResponseEntity.ok(listData);
    }

    @PostMapping("create")
    @Secured("ROLE_LOCALE_MANAGE")
    public ResponseEntity<?> create(@RequestBody SysLanguage createRequest, Principal principal) {
        log.debug("create -> " + createRequest);

        if (ObjectUtils.isEmpty(createRequest.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("code Хоосон байна.");
        }

        User user = userRepository.findByEmailAndDeletedFalse(principal.getName());
        if (user != null) {
            if (sysLanguageRepository.existsByCodeAndDeletedFalse(createRequest.getCode().toLowerCase())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("code давхцаж байна.");
            }

            SysLanguage sysLanguage = new SysLanguage();
            sysLanguage.setName(createRequest.getName());
            sysLanguage.setCode(createRequest.getCode().toLowerCase());
            sysLanguage.setOrder(createRequest.getOrder());
            sysLanguage.setActive(createRequest.isActive());
            sysLanguage.setCreatedBy(user.getId());
            sysLanguage.setCreatedDate(LocalDateTime.now());

            sysLanguageRepository.save(sysLanguage);

            return ResponseEntity.ok(sysLanguage.getId());

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }
    }

    @PostMapping("update")
    @Secured("ROLE_LOCALE_MANAGE")
    public ResponseEntity<?> update(@RequestBody SysLanguage updateRequest, Principal principal) {
        log.debug("update -> " + updateRequest);

        if (ObjectUtils.isEmpty(updateRequest.getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID хоосон байна.");

        User user = userRepository.findByEmailAndDeletedFalse(principal.getName());

        if (user != null) {
            Optional<SysLanguage> typeOptional = sysLanguageRepository.findById(updateRequest.getId());

            if (typeOptional.isPresent()) {
                SysLanguage sysLanguage = typeOptional.get();

                sysLanguage.setName(updateRequest.getName());
                sysLanguage.setCode(updateRequest.getCode().toLowerCase());
                sysLanguage.setOrder(updateRequest.getOrder());
                sysLanguage.setActive(updateRequest.isActive());
                sysLanguage.setModifiedBy(user.getId());
                sysLanguage.setModifiedDate(LocalDateTime.now());

                sysLanguage = sysLanguageRepository.save(sysLanguage);

                return ResponseEntity.ok(sysLanguage.getId());

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SysLanguage not found.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return ResponseEntity.ok().body(sysLanguageRepository.findById(id).orElse(null));
    }

    @GetMapping("get-with-code/{code}")
    public ResponseEntity<?> getWithCode(@PathVariable String code) {
        return ResponseEntity.ok().body(sysLanguageRepository.findByCode(code).orElse(null));
    }

    @PostMapping("delete")
    @Secured("ROLE_LOCALE_MANAGE")
    public ResponseEntity<?> deleteMulti(@RequestParam String id, Principal principal) {
        log.debug("delete id ->  " + id);

        User user = userRepository.findByEmailAndDeletedFalse(principal.getName());
        if (user != null) {
            Optional<SysLanguage> typeOptional = sysLanguageRepository.findById(id);
            if (typeOptional.isPresent()) {
                SysLanguage sysLanguage = typeOptional.get();
                sysLanguage.setDeleted(true);
                sysLanguage.setActive(false);
                sysLanguage.setModifiedBy(user.getId());
                sysLanguage.setModifiedDate(LocalDateTime.now());

                sysLanguageRepository.save(sysLanguage);
                return ResponseEntity.ok().body(true);
            } else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SysLanguage not found.");
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
    }

}
