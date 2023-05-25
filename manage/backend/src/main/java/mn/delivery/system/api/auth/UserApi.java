package mn.delivery.system.api.auth;

import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.BaseController;
import mn.delivery.system.api.request.antd.AntdPagination;
import mn.delivery.system.api.response.antd.AntdTableDataList;
import mn.delivery.system.dao.auth.UserDao;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.repository.auth.UserRepository;
import mn.delivery.system.api.request.CreateUserRequest;
import mn.delivery.system.exception.permission.PermissionException;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserApi extends BaseController {

    private final UserDao userDao;
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @Secured({"ROLE_USER_VIEW", "ROLE_USER_MANAGE"})
    public ResponseEntity<?> list(String role, String search, Boolean using2fa, Boolean emailVerified, Boolean phoneVerified, Boolean active, Boolean deleted, AntdPagination pagination) {
        AntdTableDataList<User> listData = new AntdTableDataList<>();

        pagination.setTotal(userDao.count(role, search, using2fa, emailVerified, phoneVerified, active, deleted));
        listData.setPagination(pagination);
        listData.setList(userDao.list(role, search, using2fa, emailVerified, phoneVerified, active, deleted, pagination.toPageRequest()));
        return ResponseEntity.ok(listData);
    }

    @PostMapping("create")
    @Secured("ROLE_USER_MANAGE")
    public ResponseEntity<?> create(@RequestBody CreateUserRequest createRequest, Locale locale, Principal principal) {
        try {
            if (ObjectUtils.isEmpty(createRequest.getEmail()) ||
                    ObjectUtils.isEmpty(createRequest.getPassword()) ||
                    ObjectUtils.isEmpty(createRequest.getRole())
            )
                return errorInvalidRequest(locale);

            User authUser = basePermissionService.user(principal, locale);

            if (userRepository.existsByEmailAndDeletedFalse(createRequest.getEmail().toLowerCase()))
                return ResponseEntity.badRequest().body("Имэйл хаяг бүртгэлтэй байна");

            log.debug("create ->" + createRequest + ", by->" + authUser.getId());

            User user = new User();
            user.setEmail(createRequest.getEmail().toLowerCase());
            user.setPassword(passwordEncoder.encode(createRequest.getPassword()));
            user.setRole(createRequest.getRole());
            user.setMobile(createRequest.getMobile());
            user.setLastName(createRequest.getLastName());
            user.setFirstName(createRequest.getFirstName());
            user.setAddress(createRequest.getAddress());
            user.setRegistryNumber(createRequest.getRegistryNumber());

            user.setActive(createRequest.isActive());
            user.setCreatedBy(authUser.getId());
            user.setCreatedDate(LocalDateTime.now());
            user = userRepository.save(user);

            return ResponseEntity.ok(user.getId());
        } catch (PermissionException e) {
            return errorPermission(locale);
        }
    }

    @Secured("ROLE_USER_MANAGE")
    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody CreateUserRequest updateRequest, Locale locale, Principal principal) {
        try {
            User authUser = basePermissionService.user(principal, locale);

            User user = userRepository.findByIdAndDeletedFalse(updateRequest.getId());
            if (user == null)
                return ResponseEntity.badRequest().body("Хэрэглэгчийн мэдээлэл олдсонгүй");

            if (userRepository.existsByEmailAndIdNotAndDeletedFalse(
                    updateRequest.getEmail().toLowerCase(),
                    updateRequest.getId()))
                return ResponseEntity.badRequest().body("Имэйл хаяг бүртгэлтэй байна");

            log.debug("update ->" + updateRequest + ", id: " + updateRequest.getId() + ", by " + authUser.getId());

            if (!ObjectUtils.isEmpty(updateRequest.getPassword()))
                user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

            user.setMobile(updateRequest.getMobile());
            user.setRole(updateRequest.getRole());
            user.setLastName(updateRequest.getLastName());
            user.setFirstName(updateRequest.getFirstName());
            user.setActive(updateRequest.isActive());

            user.setModifiedDate(LocalDateTime.now());
            user.setModifiedBy(authUser.getId());

            userRepository.save(user);
            return ResponseEntity.ok(user.getId());
        } catch (PermissionException e) {
            return errorPermission(locale);
        }
    }

    @Secured("ROLE_USER_MANAGE")
    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@RequestParam String id, Locale locale, Principal principal) {
        try {
            User authUser = basePermissionService.user(principal, locale);
            log.debug("delete: id->" + id + ", by->" + authUser.getId());

            User user = userRepository.findByIdAndDeletedFalse(id);
            if (user == null)
                return ResponseEntity.badRequest()
                        .body(messageSource.getMessage("data.notFound", null, locale));

            user.setDeleted(true);
            user.setActive(false);
//            user.setOldUsername(user.getUsername());
//            user.setUsername(user.getUsername() + "_" + user.getId());
//            user.setOldEmail(user.getEmail());
//            user.setEmail(null);
            user.setModifiedDate(LocalDateTime.now());
            user.setModifiedBy(authUser.getId());

            userRepository.save(user);
            return ResponseEntity.ok(user.getId());
        } catch (PermissionException e) {
            return errorPermission(locale);
        } catch (MongoException | DataAccessException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.database", null, locale));
        }
    }

    @Secured("ROLE_USER_MANAGE")
    @GetMapping("disable-auth/{userId}")
    public ResponseEntity<?> disableAuth(@PathVariable String userId, Locale locale, Principal principal) {
        try {
            User authUser = basePermissionService.user(principal, locale);
            log.debug("disable 2FA: userId->" + userId + ", by->" + authUser.getId());

            User user = userRepository.findByIdAndDeletedFalse(userId);
            if (user == null)
                return ResponseEntity.badRequest()
                        .body(messageSource.getMessage("data.notFound", null, locale));
            if (!user.isUsing2fa())
                return ResponseEntity.badRequest().body("Хэрэглэгчийн 2FA идэвхгүй байна");

            user.setUsing2fa(false);
            user.setSecretKey(null);
            user.setModifiedDate(LocalDateTime.now());
            user.setModifiedBy(authUser.getId());

            userRepository.save(user);
            return ResponseEntity.ok(user.getId());
        } catch (PermissionException e) {
            return errorPermission(locale);
        }
    }
}
