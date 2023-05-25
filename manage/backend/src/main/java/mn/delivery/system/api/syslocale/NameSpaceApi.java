package mn.delivery.system.api.syslocale;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.request.antd.AntdPagination;
import mn.delivery.system.api.response.antd.AntdTableDataList;
import mn.delivery.system.dao.syslocale.NameSpaceDao;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.syslocale.NameSpace;
import mn.delivery.system.repository.auth.UserRepository;
import mn.delivery.system.repository.syslocale.NameSpaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Maroon
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@Secured("ROLE_MANAGE_DEFAULT")
@RequestMapping("/v1/name-space")
public class NameSpaceApi {

    private final UserRepository userRepository;
    private final NameSpaceRepository nameSpaceRepository;
    private final NameSpaceDao nameSpaceDao;

    @GetMapping
    @Secured("ROLE_LOCALE_VIEW")
    public ResponseEntity<?> list(String name, String value, Boolean active, AntdPagination pagination) {
        AntdTableDataList<NameSpace> listData = new AntdTableDataList<>();

        pagination.setTotal(nameSpaceDao.count(name, value, active));
        listData.setPagination(pagination);
        listData.setList(nameSpaceDao.list(name, value, active, pagination.toPageRequest()));

        return ResponseEntity.ok(listData);
    }

    @GetMapping("select")
    public ResponseEntity<?> select(String name, String value, Boolean active) {

        Iterable<NameSpace> listData = nameSpaceDao.list(name, value, active, null);
        return ResponseEntity.ok(listData);
    }

    @PostMapping("create")
    @Secured("ROLE_LOCALE_MANAGE")
    public ResponseEntity<?> create(@RequestBody List<NameSpace> createRequests, Principal principal) {

        User user = userRepository.findByEmailAndDeletedFalse(principal.getName());
        if (user != null) {
            for (NameSpace item : createRequests) {

                log.debug("creating nameSpace -> " + item);

                if (ObjectUtils.isEmpty(item.getValue())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(item.getName() + " утга Хоосон байна.");
                }

                if (nameSpaceRepository.existsByValueAndDeletedFalse(item.getValue().toLowerCase())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(item.getName() + " утга давхцаж байна.");
                }
                item.setCreatedBy(user.getId());
                item.setCreatedDate(LocalDateTime.now());
            }
            nameSpaceRepository.saveAll(createRequests);

            return ResponseEntity.ok(true);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }
    }

    @PostMapping("update")
    @Secured("ROLE_LOCALE_MANAGE")
    public ResponseEntity<?> update(@RequestBody NameSpace updateRequest, Principal principal) {
        log.debug("updating nameSpace -> " + updateRequest);

        if (ObjectUtils.isEmpty(updateRequest.getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID хоосон байна.");

        User user = userRepository.findByEmailAndDeletedFalse(principal.getName());

        if (user != null) {
            Optional<NameSpace> typeOptional = nameSpaceRepository.findById(updateRequest.getId());

            if (typeOptional.isPresent()) {
                NameSpace nameSpace = typeOptional.get();

                nameSpace.setName(updateRequest.getName());
                nameSpace.setValue(updateRequest.getValue().toLowerCase());
                nameSpace.setActive(updateRequest.isActive());
                nameSpace.setModifiedBy(user.getId());
                nameSpace.setModifiedDate(LocalDateTime.now());

                nameSpace = nameSpaceRepository.save(nameSpace);

                return ResponseEntity.ok(nameSpace.getId());

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("NameSpace not found.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return ResponseEntity.ok().body(nameSpaceRepository.findById(id).orElse(null));
    }

    @GetMapping("get-with-value/{value}")
    public ResponseEntity<?> getWithNameSpace(@PathVariable String value) {
        return ResponseEntity.ok().body(nameSpaceRepository.findByValue(value).orElse(null));
    }

    @PostMapping("delete")
    @Secured("ROLE_LOCALE_MANAGE")
    public ResponseEntity<?> deleteMulti(@RequestParam String id, Principal principal) {
        log.debug("delete id ->  " + id);

        User user = userRepository.findByEmailAndDeletedFalse(principal.getName());
        if (user != null) {
            Optional<NameSpace> typeOptional = nameSpaceRepository.findById(id);
            if (typeOptional.isPresent()) {
                NameSpace nameSpace = typeOptional.get();
                nameSpace.setDeleted(true);
                nameSpace.setActive(false);
                nameSpace.setModifiedDate(LocalDateTime.now());
                nameSpace.setModifiedBy(user.getId());

                nameSpaceRepository.save(nameSpace);
                return ResponseEntity.ok().body(true);
            } else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("NameSpace not found.");
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
    }

}
